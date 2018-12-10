/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:26 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找应用在指定环境的所有配置value服务
 */
@Service
public class FindAppProfilePropertyValuesService {
    // info转换器
    private static final Converter<PropertyValue, PropertyValueInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(PropertyValueInfo.class);

    @Autowired
    private PropertyValueDao propertyValueDao;

    @ServiceBefore
    public void before(ServiceContext<FindAppProfilePropertyValuesOrder, FindAppProfilePropertyValuesResult> context) {
        FindAppProfilePropertyValuesOrder order = context.getOrder();

        AppInfo app = AppUtils.findApp(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]", order.getAppId()));
        }
        ProfileInfo profile = ProfileUtils.findProfile(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindAppProfilePropertyValuesOrder, FindAppProfilePropertyValuesResult> context) {
        FindAppProfilePropertyValuesOrder order = context.getOrder();
        FindAppProfilePropertyValuesResult result = context.getResult();

        List<PropertyValue> propertyValues = propertyValueDao.findByAppIdAndProfileId(order.getAppId(), order.getProfileId());
        // 忽略作用域不合要求的value
        propertyValues.removeIf(propertyValue -> propertyValue.getScope().compareTo(order.getMinScope()) < 0);
        // 设置result
        for (PropertyValue propertyValue : propertyValues) {
            result.addPropertyValue(INFO_CONVERTER.convert(propertyValue));
        }
    }
}
