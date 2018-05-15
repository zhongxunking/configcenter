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
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindPropertiesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
        // 校验应用
        for (String appId : new HashSet<>(Arrays.asList(order.getAppId(), order.getQueriedAppId()))) {
            App app = appDao.findByAppId(appId);
            if (app == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]", appId));
            }
        }
        // 校验环境
        Profile profile = profileDao.findByProfileId(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindPropertiesOrder, FindPropertiesResult> context) {
        FindPropertiesOrder order = context.getOrder();
        FindPropertiesResult result = context.getResult();
        // 获取继承的应用id
        Set<String> inheritAppIds = getInheritAppIds(order.getAppId());
        // 获取应用配置
        Map<String, String> properties = getAppProperties(order.getQueriedAppId(), order.getProfileId(), inheritAppIds);

        result.setProperties(properties);
    }

    // 获取继承的应用id
    private Set<String> getInheritAppIds(String appId) {
        Set<String> appIds = new HashSet<>();
        while (appId != null) {
            appIds.add(appId);
            appId = appDao.findByAppId(appId).getParent();
        }
        return appIds;
    }

    // 获取应用配置
    private Map<String, String> getAppProperties(String appId, String profileId, Set<String> allPropertiesAppIds) {
        Map<String, String> properties = new HashMap<>();
        while (appId != null) {
            Map<String, String> temp = getAppSelfProperties(appId, profileId, !allPropertiesAppIds.contains(appId));
            temp.putAll(properties);
            properties = temp;

            appId = appDao.findByAppId(appId).getParent();
        }
        return properties;
    }

    // 获取应用自己的配置
    private Map<String, String> getAppSelfProperties(String appId, String profileId, boolean onlyOutward) {
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
