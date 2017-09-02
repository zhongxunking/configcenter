/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.order.manage.SetPropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.SetPropertyValueResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 设置属性value服务
 */
@Service(enableTx = true)
public class SetPropertyValueService {
    @Autowired
    private PropertyValueDao propertyValueDao;

    @ServiceExecute
    public void execute(ServiceContext<SetPropertyValueOrder, SetPropertyValueResult> context) {
        SetPropertyValueOrder order = context.getOrder();

        PropertyValue propertyValue = propertyValueDao.findLockByProfileCodeAndAppCodeAndKey(order.getProfileCode(), order.getAppCode(), order.getKey());
        if (propertyValue == null) {
            propertyValue = buildPropertyValue(order);
        } else {
            propertyValue.setValue(order.getValue());
        }
        propertyValueDao.save(propertyValue);
    }

    //构建属性value
    private PropertyValue buildPropertyValue(SetPropertyValueOrder setPropertyValueOrder) {
        PropertyValue propertyValue = new PropertyValue();
        BeanUtils.copyProperties(setPropertyValueOrder, propertyValue);
        return propertyValue;
    }
}
