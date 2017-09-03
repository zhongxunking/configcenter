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
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
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

import java.util.*;

/**
 * 查询应用在特定环境中的配置服务
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

        Profile profile = profileDao.findByProfileCode(order.getProfileCode());
        if (profile == null) {
            throw new AntBekitException(Status.SUCCESS, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileCode()));
        }
        App app = appDao.findByAppCode(order.getAppCode());
        if (app == null) {
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

    // 获取特定应用的属性value
    private List<PropertyValue> getAppPropertyValues(QueryAppProfilePropertyOrder order) {
        List<PropertyValue> propertyValues = propertyValueDao.findByProfileCodeAndAppCode(order.getProfileCode(), order.getAppCode());

        if (order.isOnlyCommon()) {
            // 如果只查询公共属性，则剔除掉私有属性
            Set<String> commonKeys = new HashSet<>();
            for (PropertyKey propertyKey : propertyKeyDao.findByAppCodeAndCommon(order.getAppCode(), true)) {
                commonKeys.add(propertyKey.getKey());
            }
            List<PropertyValue> commonPropertyValues = new ArrayList<>();
            for (PropertyValue propertyValue : propertyValues) {
                if (commonKeys.contains(propertyValue.getKey())) {
                    commonPropertyValues.add(propertyValue);
                }
            }

            propertyValues = commonPropertyValues;
        }

        return propertyValues;
    }
}
