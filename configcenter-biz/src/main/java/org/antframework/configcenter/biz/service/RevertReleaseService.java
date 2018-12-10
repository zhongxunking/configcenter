/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-09 16:30 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.*;
import org.antframework.configcenter.biz.util.PropertyValueUtils;
import org.antframework.configcenter.biz.util.RefreshUtils;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.antframework.configcenter.facade.order.RevertReleaseOrder;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 回滚发布服务
 */
@Service(enableTx = true)
public class RevertReleaseService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private ReleaseDao releaseDao;
    @Autowired
    private PropertyValueService propertyValueService;

    @ServiceExecute
    public void execute(ServiceContext<RevertReleaseOrder, EmptyResult> context) {
        RevertReleaseOrder order = context.getOrder();
        // 校验入参
        App app = appDao.findLockByAppId(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
        Profile profile = profileDao.findLockByProfileId(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileId()));
        }
        Release release = null;
        if (order.getVersion() > ReleaseConstant.ORIGIN_VERSION) {
            release = releaseDao.findLockByAppIdAndProfileIdAndVersion(order.getAppId(), order.getProfileId(), order.getVersion());
            if (release == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("回滚到的目标发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), order.getVersion()));
            }
        }
        // 回滚配置value
        revertPropertyValues(order.getAppId(), order.getProfileId(), release);
        // 回滚发布
        releaseDao.deleteByAppIdAndProfileIdAndVersionGreaterThan(order.getAppId(), order.getProfileId(), order.getVersion());
    }

    // 回滚配置value
    private void revertPropertyValues(String appId, String profileId, Release release) {
        // 删除现有配置value
        PropertyValueUtils.deleteAppProfilePropertyValues(appId, profileId);
        // 判断是否需要重建配置value
        if (release == null) {
            return;
        }
        // 使用发布重建配置value
        for (Property property : release.getProperties()) {
            AddOrModifyPropertyValueOrder order = new AddOrModifyPropertyValueOrder();
            BeanUtils.copyProperties(property, order);
            order.setAppId(appId);
            order.setProfileId(profileId);

            EmptyResult result = propertyValueService.addOrModifyPropertyValue(order);
            FacadeUtils.assertSuccess(result);
        }
    }

    @ServiceAfter
    public void after(ServiceContext<RevertReleaseOrder, EmptyResult> context) {
        RevertReleaseOrder order = context.getOrder();
        // 刷新客户端
        RefreshUtils.refreshClients(order.getAppId(), order.getProfileId());
    }
}
