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
import org.antframework.configcenter.biz.util.Profiles;
import org.antframework.configcenter.biz.util.PropertyValues;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
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

    @ServiceExecute
    public void execute(ServiceContext<DeletePropertyKeyOrder, EmptyResult> context) {
        DeletePropertyKeyOrder order = context.getOrder();

        PropertyKey propertyKey = propertyKeyDao.findLockByAppIdAndKey(order.getAppId(), order.getKey());
        if (propertyKey == null) {
            return;
        }
        // 删除该key在所有环境的value
        for (ProfileInfo profile : Profiles.findAllProfiles()) {
            PropertyValues.deletePropertyValue(order.getAppId(), order.getKey(), profile.getProfileId());
        }
        // 删除key
        propertyKeyDao.delete(propertyKey);
    }
}
