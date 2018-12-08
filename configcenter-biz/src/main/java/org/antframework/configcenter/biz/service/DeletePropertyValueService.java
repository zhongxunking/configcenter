/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 22:50 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除配置value服务
 */
@Service(enableTx = true)
public class DeletePropertyValueService {
    @Autowired
    private PropertyValueDao propertyValueDao;

    @ServiceExecute
    public void execute(ServiceContext<DeletePropertyValueOrder, EmptyResult> context) {
        DeletePropertyValueOrder order = context.getOrder();

        PropertyValue propertyValue = propertyValueDao.findLockByAppIdAndKeyAndProfileId(order.getAppId(), order.getKey(), order.getProfileId());
        if (propertyValue != null) {
            propertyValueDao.delete(propertyValue);
        }
    }
}
