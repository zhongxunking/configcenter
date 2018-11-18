/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:31 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.biz.util.PropertyKeyUtils;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ProfileProperty;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.order.FindAppSelfPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
import org.antframework.configcenter.facade.result.FindAppSelfPropertiesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查找应用自己的在指定环境中的配置服务
 */
@Service
public class FindAppSelfPropertiesService {
    @Autowired
    private PropertyValueService propertyValueService;

    @ServiceExecute
    public void execute(ServiceContext<FindAppSelfPropertiesOrder, FindAppSelfPropertiesResult> context) {
        FindAppSelfPropertiesOrder order = context.getOrder();
        FindAppSelfPropertiesResult result = context.getResult();
        // 获取应用的key和对应的作用域
        Map<String, Scope> keyScopes = getKeyScopes(order.getAppId(), order.getMinScope());
        // 获取每个继承的环境中的配置
        for (ProfileInfo profile : ProfileUtils.findInheritedProfiles(order.getProfileId())) {
            // 获取环境中的配置
            ProfileProperty profileProperty = new ProfileProperty();
            profileProperty.setProfileId(profile.getProfileId());
            for (PropertyValueInfo propertyValue : getPropertyValues(order.getAppId(), profile.getProfileId())) {
                if (!keyScopes.containsKey(propertyValue.getKey())) {
                    continue;
                }
                Scope scope = keyScopes.get(propertyValue.getKey());
                profileProperty.addProperty(new Property(propertyValue.getKey(), propertyValue.getValue(), scope));
            }
            result.addProfileProperty(profileProperty);
        }
    }

    // 获取应用在指定环境的所有配置value
    private List<PropertyValueInfo> getPropertyValues(String appId, String profileId) {
        FindAppProfilePropertyValuesOrder order = new FindAppProfilePropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        FindAppProfilePropertyValuesResult result = propertyValueService.findAppProfilePropertyValues(order);
        FacadeUtils.assertSuccess(result);
        return result.getPropertyValues();
    }

    // 获取应用的key和对应的作用域
    private Map<String, Scope> getKeyScopes(String appId, Scope minScope) {
        Map<String, Scope> keyScopes = new HashMap<>();
        for (PropertyKeyInfo propertyKey : PropertyKeyUtils.findAppPropertyKeys(appId, minScope)) {
            keyScopes.put(propertyKey.getKey(), propertyKey.getScope());
        }
        return keyScopes;
    }
}
