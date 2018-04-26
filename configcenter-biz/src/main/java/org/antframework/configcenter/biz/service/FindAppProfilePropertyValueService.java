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
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValueOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValueResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找应用在指定环境的所有属性value服务
 */
@Service
public class FindAppProfilePropertyValueService {
    // info转换器
    private static final Converter<PropertyValue, PropertyValueInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(PropertyValueInfo.class);

    @Autowired
    private AppDao appDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private PropertyValueDao propertyValueDao;

    @ServiceBefore
    public void before(ServiceContext<FindAppProfilePropertyValueOrder, FindAppProfilePropertyValueResult> context) {
        FindAppProfilePropertyValueOrder order = context.getOrder();

        App app = appDao.findByAppCode(order.getAppCode());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppCode()));
        }
        Profile profile = profileDao.findByProfileCode(order.getProfileCode());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileCode()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindAppProfilePropertyValueOrder, FindAppProfilePropertyValueResult> context) {
        FindAppProfilePropertyValueOrder order = context.getOrder();
        FindAppProfilePropertyValueResult result = context.getResult();

        List<PropertyValue> propertyValues = propertyValueDao.findByAppCodeAndProfileCode(order.getAppCode(), order.getProfileCode());
        for (PropertyValue propertyValue : propertyValues) {
            result.addInfo(INFO_CONVERTER.convert(propertyValue));
        }
    }
}
