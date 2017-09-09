/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:31 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.order.manage.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.DeletePropertyValueResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除属性value服务
 */
@Service(enableTx = true)
public class DeletePropertyValueService {
    @Autowired
    private PropertyValueDao propertyValueDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<DeletePropertyValueOrder, DeletePropertyValueResult> context) {
        DeletePropertyValueOrder order = context.getOrder();

        PropertyValue propertyValue = propertyValueDao.findLockByProfileCodeAndAppCodeAndKey(order.getProfileCode(), order.getAppCode(), order.getKey());
        if (propertyValue != null) {
            propertyValueDao.delete(propertyValue);
        }
    }

    @ServiceAfter
    public void after(ServiceContext<DeletePropertyValueOrder, DeletePropertyValueResult> context) {
        DeletePropertyValueOrder order = context.getOrder();

        zkTemplate.setData(ZkTemplate.buildPath(order.getProfileCode(), order.getAppCode()), null);
    }
}
