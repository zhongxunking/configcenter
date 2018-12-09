/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:40 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.*;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.PropertyValueUtils;
import org.antframework.configcenter.biz.util.RefreshUtils;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.DeleteProfileOrder;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.RevertReleaseOrder;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除环境服务
 */
@Service(enableTx = true)
public class DeleteProfileService {
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ReleaseService releaseService;

    @ServiceExecute
    public void execute(ServiceContext<DeleteProfileOrder, EmptyResult> context) {
        DeleteProfileOrder order = context.getOrder();

        Profile profile = profileDao.findLockByProfileId(order.getProfileId());
        if (profile == null) {
            return;
        }
        if (profileDao.existsByParent(order.getProfileId())) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("环境[%s]存在子环境，不能删除", order.getProfileId()));
        }
        // 删除所有应用在该环境下的所有配置value
        for (AppInfo app : AppUtils.findAllApps()) {
            deletePropertyValues(app.getAppId(), order.getProfileId());
            deleteReleases(app.getAppId(), order.getProfileId());
        }
        // 删除环境
        profileDao.delete(profile);
    }

    // 删除应有在指定环境的所有配置value
    private void deletePropertyValues(String appId, String profileId) {
        for (PropertyValueInfo propertyValue : PropertyValueUtils.findAppProfilePropertyValues(appId, profileId)) {
            DeletePropertyValueOrder order = new DeletePropertyValueOrder();
            BeanUtils.copyProperties(propertyValue, order);

            EmptyResult result = propertyValueService.deletePropertyValue(order);
            FacadeUtils.assertSuccess(result);
        }
    }

    // 删除应用在指定环境的所有发布
    private void deleteReleases(String appId, String profileId) {
        RevertReleaseOrder order = new RevertReleaseOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setVersion(ReleaseConstant.ORIGIN_VERSION);

        EmptyResult result = releaseService.revertRelease(order);
        FacadeUtils.assertSuccess(result);
    }

    @ServiceAfter
    public void after(ServiceContext<DeleteProfileOrder, EmptyResult> context) {
        // 刷新zookeeper
        RefreshUtils.refreshZk();
    }
}
