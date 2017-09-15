/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.order.manage.SetPropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.SetPropertyValueResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
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
    private ProfileDao profileDao;
    @Autowired
    private PropertyKeyDao propertyKeyDao;
    @Autowired
    private PropertyValueDao propertyValueDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<SetPropertyValueOrder, SetPropertyValueResult> context) {
        SetPropertyValueOrder order = context.getOrder();

        PropertyKey propertyKey = propertyKeyDao.findLockByAppCodeAndKey(order.getAppCode(), order.getKey());
        if (propertyKey == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]属性key[%s]", order.getAppCode(), order.getKey()));
        }
        Profile profile = profileDao.findLockByProfileCode(order.getProfileCode());
        if (profile == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileCode()));
        }

        PropertyValue propertyValue = propertyValueDao.findLockByAppCodeAndKeyAndProfileCode(order.getAppCode(), order.getKey(), order.getProfileCode());
        if (propertyValue == null) {
            propertyValue = buildPropertyValue(order);
        } else {
            propertyValue.setValue(order.getValue());
        }
        propertyValueDao.save(propertyValue);
    }

    @ServiceAfter
    public void after(ServiceContext<SetPropertyValueOrder, SetPropertyValueResult> context) {
        SetPropertyValueOrder order = context.getOrder();

        zkTemplate.setData(ZkTemplate.buildPath(order.getProfileCode(), order.getAppCode()), null);
    }

    //构建属性value
    private PropertyValue buildPropertyValue(SetPropertyValueOrder setPropertyValueOrder) {
        PropertyValue propertyValue = new PropertyValue();
        BeanUtils.copyProperties(setPropertyValueOrder, propertyValue);
        return propertyValue;
    }
}
