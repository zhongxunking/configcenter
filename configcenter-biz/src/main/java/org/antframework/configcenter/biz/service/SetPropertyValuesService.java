/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.common.ZkUtils;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 设置多个属性value服务
 */
@Service(enableTx = true)
public class SetPropertyValuesService {
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private PropertyKeyDao propertyKeyDao;
    @Autowired
    private PropertyValueDao propertyValueDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<SetPropertyValuesOrder, EmptyResult> context) {
        SetPropertyValuesOrder order = context.getOrder();

        Profile profile = profileDao.findLockByProfileCode(order.getProfileCode());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileCode()));
        }

        for (SetPropertyValuesOrder.KeyValue keyValue : order.getKeyValues()) {
            setSingleValue(order, keyValue);
        }
    }

    @ServiceAfter
    public void after(ServiceContext<SetPropertyValuesOrder, EmptyResult> context) {
        SetPropertyValuesOrder order = context.getOrder();

        zkTemplate.setData(ZkTemplate.buildPath(order.getProfileCode(), order.getAppCode()), ZkUtils.getCurrentDate());
    }

    // 设置单个属性value
    private void setSingleValue(SetPropertyValuesOrder order, SetPropertyValuesOrder.KeyValue keyValue) {
        PropertyKey propertyKey = propertyKeyDao.findLockByAppCodeAndKey(order.getAppCode(), keyValue.getKey());
        if (propertyKey == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]属性key[%s]", order.getAppCode(), keyValue.getKey()));
        }

        PropertyValue propertyValue = propertyValueDao.findLockByAppCodeAndKeyAndProfileCode(order.getAppCode(), keyValue.getKey(), order.getProfileCode());
        if (propertyValue == null) {
            propertyValue = buildPropertyValue(order, keyValue);
        } else {
            propertyValue.setValue(keyValue.getValue());
        }
        propertyValueDao.save(propertyValue);
    }

    //构建属性value
    private PropertyValue buildPropertyValue(SetPropertyValuesOrder setPropertyValueOrder, SetPropertyValuesOrder.KeyValue keyValue) {
        PropertyValue propertyValue = new PropertyValue();
        BeanUtils.copyProperties(setPropertyValueOrder, propertyValue);
        BeanUtils.copyProperties(keyValue, propertyValue);
        return propertyValue;
    }
}
