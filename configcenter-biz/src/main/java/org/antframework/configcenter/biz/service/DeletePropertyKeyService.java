/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:52 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 删除配置key服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeletePropertyKeyService {
    // 配置key dao
    private final PropertyKeyDao propertyKeyDao;
    // 配置value服务
    private final PropertyValueService propertyValueService;

    @ServiceExecute
    public void execute(ServiceContext<DeletePropertyKeyOrder, EmptyResult> context) {
        DeletePropertyKeyOrder order = context.getOrder();

        PropertyKey propertyKey = propertyKeyDao.findLockByAppIdAndKey(order.getAppId(), order.getKey());
        if (propertyKey == null) {
            return;
        }
        // 删除该key在所有环境的value
        for (ProfileInfo profile : ProfileUtils.findAllProfiles()) {
            deletePropertyValue(order.getAppId(), order.getKey(), profile.getProfileId());
        }
        // 删除key
        propertyKeyDao.delete(propertyKey);
    }

    // 删除配置value
    private void deletePropertyValue(String appId, String key, String profileId) {
        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setAppId(appId);
        order.setKey(key);
        order.setProfileId(profileId);

        EmptyResult result = propertyValueService.deletePropertyValue(order);
        FacadeUtils.assertSuccess(result);
    }
}
