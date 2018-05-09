/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:51 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
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
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindPropertiesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查找应用在特定环境中的配置服务
 */
@Service
public class FindPropertiesService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private PropertyKeyDao propertyKeyDao;
    @Autowired
    private PropertyValueDao propertyValueDao;

    @ServiceBefore
    public void before(ServiceContext<FindPropertiesOrder, FindPropertiesResult> context) {
        FindPropertiesOrder order = context.getOrder();

        App app = appDao.findByAppId(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]", order.getAppId()));
        }
        Profile profile = profileDao.findByProfileId(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindPropertiesOrder, FindPropertiesResult> context) {
        FindPropertiesOrder order = context.getOrder();
        FindPropertiesResult result = context.getResult();
        // 获取公共配置
        Map<String, String> properties = getAppProperties(ConfigService.COMMON_APP_ID, order.getProfileId(), false);
        // 获取应用配置（覆盖公共配置）
        properties.putAll(getAppProperties(order.getAppId(), order.getProfileId(), order.isOnlyOutward()));

        result.setProperties(properties);
    }

    // 获取应用配置
    private Map<String, String> getAppProperties(String appId, String profileId, boolean onlyOutward) {
        Map<String, String> properties = new HashMap<>();

        List<PropertyKey> propertyKeys = propertyKeyDao.findByAppId(appId);
        for (PropertyKey propertyKey : propertyKeys) {
            if (onlyOutward && !propertyKey.getOutward()) {
                continue;
            }
            properties.put(propertyKey.getKey(), null);
        }
        List<PropertyValue> propertyValues = propertyValueDao.findByAppIdAndProfileId(appId, profileId);
        for (PropertyValue propertyValue : propertyValues) {
            if (properties.containsKey(propertyValue.getKey())) {
                properties.put(propertyValue.getKey(), propertyValue.getValue());
            }
        }

        return properties;
    }
}
