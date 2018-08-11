/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:31 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.biz.util.PropertyKeyUtils;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.order.FindAppSelfPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
import org.antframework.configcenter.facade.result.FindAppSelfPropertiesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查找应用自己的在特定环境中的配置服务
 */
@Service
public class FindAppSelfPropertiesService {
    @Autowired
    private PropertyValueService propertyValueService;

    @ServiceExecute
    public void execute(ServiceContext<FindAppSelfPropertiesOrder, FindAppSelfPropertiesResult> context) {
        FindAppSelfPropertiesOrder order = context.getOrder();
        FindAppSelfPropertiesResult result = context.getResult();
        // 获取应用自己的属性key
        List<PropertyKeyInfo> propertyKeys = PropertyKeyUtils.findAppPropertyKeys(order.getAppId(), order.getMinScope());
        // 获取应用自己的属性value
        Map<String, String> values = getAppProfilePropertyValues(order.getAppId(), order.getProfileId());
        // 组装属性
        for (PropertyKeyInfo propertyKey : propertyKeys) {
            // 添加属性
            String value = values.get(propertyKey.getKey());
            result.addProperty(new Property(propertyKey.getKey(), value, propertyKey.getScope()));
        }
    }

    // 获取应用在特定环境的所有属性value
    private Map<String, String> getAppProfilePropertyValues(String appId, String profileId) {
        FindAppProfilePropertyValuesOrder order = new FindAppProfilePropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        FindAppProfilePropertyValuesResult result = propertyValueService.findAppProfilePropertyValues(order);
        FacadeUtils.assertSuccess(result);

        Map<String, String> values = new HashMap<>();
        for (PropertyValueInfo propertyValue : result.getPropertyValues()) {
            values.put(propertyValue.getKey(), propertyValue.getValue());
        }
        return values;
    }
}
