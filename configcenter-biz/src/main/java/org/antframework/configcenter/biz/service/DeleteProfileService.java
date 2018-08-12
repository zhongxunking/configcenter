/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:40 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.PropertyKeyUtils;
import org.antframework.configcenter.biz.util.RefreshUtils;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.DeleteProfileOrder;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
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

    @ServiceExecute
    public void execute(ServiceContext<DeleteProfileOrder, EmptyResult> context) {
        DeleteProfileOrder order = context.getOrder();

        Profile profile = profileDao.findLockByProfileId(order.getProfileId());
        if (profile == null) {
            return;
        }
        // 删除所有应用在该环境下的所有属性value
        for (AppInfo app : AppUtils.findAllApps()) {
            deleteAppProfileAllPropertyValues(app.getAppId(), order.getProfileId());
        }
        // 删除环境
        profileDao.delete(profile);
    }

    // 删除应有在指定环境的所有属性value
    private void deleteAppProfileAllPropertyValues(String appId, String profileId) {
        SetPropertyValuesOrder order = new SetPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        for (PropertyKeyInfo propertyKey : PropertyKeyUtils.findAppPropertyKeys(appId, Scope.PRIVATE)) {
            SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
            keyValue.setKey(propertyKey.getKey());
            keyValue.setValue(null);

            order.addKeyValue(keyValue);
        }
        if (order.getKeyValues().size() <= 0) {
            return;
        }

        EmptyResult result = propertyValueService.setPropertyValues(order);
        FacadeUtils.assertSuccess(result);
    }

    @ServiceAfter
    public void after(ServiceContext<DeleteProfileOrder, EmptyResult> context) {
        // 刷新zookeeper
        RefreshUtils.refreshZk();
    }
}
