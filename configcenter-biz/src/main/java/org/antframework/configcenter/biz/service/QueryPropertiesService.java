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
import org.antframework.configcenter.facade.order.QueryPropertiesOrder;
import org.antframework.configcenter.facade.result.QueryPropertiesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceCheck;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询应用在特定环境中的配置服务
 */
@Service
public class QueryPropertiesService {
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private PropertyValueDao propertyValueDao;
    @Autowired
    private PropertyKeyDao propertyKeyDao;

    @ServiceCheck
    public void check(ServiceContext<QueryPropertiesOrder, QueryPropertiesResult> context) {
        QueryPropertiesOrder order = context.getOrder();

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
    public void execute(ServiceContext<QueryPropertiesOrder, QueryPropertiesResult> context) {
        QueryPropertiesOrder order = context.getOrder();
        QueryPropertiesResult result = context.getResult();
        // 获取公共配置
        Map<String, String> properties = getAppProperties(ConfigService.COMMON_APP_CODE, order.getProfileCode(), false);
        // 获取应用配置（覆盖公共配置）
        properties.putAll(getAppProperties(order.getAppCode(), order.getProfileCode(), order.isOnlyCommon()));

        result.setProperties(properties);
    }

    // 获取应用配置
    private Map<String, String> getAppProperties(String appCode, String profileCode, boolean onlyCommon) {
        Map<String, String> properties = new HashMap<>();

        List<PropertyKey> propertyKeys = propertyKeyDao.findByAppCode(appCode);
        for (PropertyKey propertyKey : propertyKeys) {
            if (onlyCommon && !propertyKey.getCommon()) {
                continue;
            }
            properties.put(propertyKey.getKey(), null);
        }
        List<PropertyValue> propertyValues = propertyValueDao.findByProfileCodeAndAppCode(profileCode, appCode);
        for (PropertyValue propertyValue : propertyValues) {
            if (properties.containsKey(propertyValue.getKey())) {
                properties.put(propertyValue.getKey(), propertyValue.getValue());
            }
        }

        return properties;
    }
}
