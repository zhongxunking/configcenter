/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:42 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.order.manage.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyPropertyKeyResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service(enableTx = true)
public class AddOrModifyPropertyKeyService {
    @Autowired
    private PropertyKeyDao propertyKeyDao;

    @ServiceExecute
    public void serviceExecute(ServiceContext<AddOrModifyPropertyKeyOrder, AddOrModifyPropertyKeyResult> serviceContext) {
        AddOrModifyPropertyKeyOrder order = serviceContext.getOrder();

        PropertyKey propertyKey = propertyKeyDao.findLockByAppCodeAndKey(order.getAppCode(), order.getKey());
        if (propertyKey == null) {
            propertyKey = buildPropertyKey(order);
        } else {
            propertyKey.setMemo(order.getMemo());
        }
        propertyKeyDao.save(propertyKey);
    }

    private PropertyKey buildPropertyKey(AddOrModifyPropertyKeyOrder addOrModifyPropertyKeyOrder) {
        PropertyKey propertyKey = new PropertyKey();
        BeanUtils.copyProperties(addOrModifyPropertyKeyOrder, propertyKey);
        return propertyKey;
    }
}
