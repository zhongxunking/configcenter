/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:51 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.QueryAppProfilePropertyOrder;
import org.antframework.configcenter.facade.result.QueryAppProfilePropertyResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceCheck;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class QueryAppProfilePropertyService {
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private PropertyValueDao propertyValueDao;
    @Autowired
    private PropertyKeyDao propertyKeyDao;

    @ServiceCheck
    public void check(ServiceContext<QueryAppProfilePropertyOrder, QueryAppProfilePropertyResult> context) {
        QueryAppProfilePropertyOrder order = context.getOrder();

        if (profileDao.findByProfileCode(order.getProfileCode()) == null) {
            throw new AntBekitException(Status.SUCCESS, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileCode()));
        }
        if (appDao.findByAppCode(order.getAppCode()) == null) {
            throw new AntBekitException(Status.SUCCESS, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]", order.getAppCode()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<QueryAppProfilePropertyOrder, QueryAppProfilePropertyResult> context) {
        QueryAppProfilePropertyOrder order = context.getOrder();
        QueryAppProfilePropertyResult result = context.getResult();

        List<PropertyValue> propertyValues = propertyValueDao.findByProfileCodeAndAppCode(order.getProfileCode(), ConfigService.PROFILE_COMMON_APP_CODE);
        propertyValues.addAll(getAppPropertyValues(order));

        Map<String, String> properties = new HashMap<>();
        for (PropertyValue propertyValue : propertyValues) {
            properties.put(propertyValue.getKey(), propertyValue.getValue());
        }

        result.setProperties(properties);
    }

    private List<PropertyValue> getAppPropertyValues(QueryAppProfilePropertyOrder order) {
        List<PropertyValue> propertyValues = new ArrayList<>();

        if (!order.isOnlyCommon()) {
            propertyValues.addAll(propertyValueDao.findByProfileCodeAndAppCode(order.getProfileCode(), order.getAppCode()));
        } else {
            List<PropertyKey> propertyKeys = propertyKeyDao.findByAppCodeAndCommon(order.getAppCode(), true);
            for (PropertyKey propertyKey : propertyKeys) {
                PropertyValue propertyValue = propertyValueDao.findByProfileCodeAndAppCodeAndKey(order.getProfileCode(), order.getAppCode(), propertyKey.getKey());
                if (propertyValue != null) {
                    propertyValues.add(propertyValue);
                }
            }
        }

        return propertyValues;
    }
}
