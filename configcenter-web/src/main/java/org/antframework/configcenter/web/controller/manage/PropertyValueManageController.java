/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:38 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.*;
import org.antframework.configcenter.facade.api.AppService;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.FindAppSelfPropertiesOrder;
import org.antframework.configcenter.facade.order.FindInheritedAppsOrder;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppSelfPropertiesResult;
import org.antframework.configcenter.facade.result.FindInheritedAppsResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.manager.web.common.ManagerAssert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性value管理controller
 */
@RestController
@RequestMapping("/manage/propertyValue")
public class PropertyValueManageController {
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private AppService appService;
    @Autowired
    private ConfigService configService;

    /**
     * 设置多个属性value
     *
     * @param appId     应用id（必须）
     * @param profileId 环境id（必须）
     * @param keys      一个或多个key（必须）
     * @param values    与keys数量对应的value（必须）
     */
    @RequestMapping("/setPropertyValue")
    public EmptyResult setPropertyValues(String appId, String profileId, String[] keys, String[] values) {
        if (keys.length != values.length) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "属性key和value数量不相等");
        }
        ManagerAssert.adminOrHaveRelation(appId);
        SetPropertyValuesOrder order = new SetPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        for (int i = 0; i < keys.length; i++) {
            SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
            keyValue.setKey(keys[i]);
            keyValue.setValue(values[i]);
            order.addKeyValue(keyValue);
        }

        return propertyValueService.setPropertyValues(order);
    }

    /**
     * 查找应用继承的配置
     *
     * @param appId     应用id
     * @param profileId 环境id
     */
    @RequestMapping("/findInheritedProperties")
    public FindInheritedPropertiesResult findInheritedProperties(String appId, String profileId) {
        ManagerAssert.adminOrHaveRelation(appId);

        FindInheritedPropertiesResult result = new FindInheritedPropertiesResult();
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
        for (AppInfo app : getInheritedApp(appId)) {
            result.addAppProperties(getAppProperties(app.getAppId(), profileId, appId));
        }

        return result;
    }

    // 获取应用继承的所有应用
    private List<AppInfo> getInheritedApp(String appId) {
        FindInheritedAppsOrder order = new FindInheritedAppsOrder();
        order.setAppId(appId);

        FindInheritedAppsResult result = appService.findInheritedApps(order);
        if (!result.isSuccess()) {
            throw new BizException(Status.FAIL, result.getCode(), result.getMessage());
        }
        return result.getInheritedApps();
    }

    // 获取应用的配置
    private FindInheritedPropertiesResult.AppProperties getAppProperties(String appId, String profileId, String mainAppId) {
        Scope minScope = Scope.PRIVATE;
        if (!StringUtils.equals(appId, mainAppId)) {
            minScope = Scope.PROTECTED;
        }

        FindAppSelfPropertiesOrder order = new FindAppSelfPropertiesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setMinScope(minScope);

        FindAppSelfPropertiesResult result = configService.findAppSelfProperties(order);
        if (!result.isSuccess()) {
            throw new BizException(Status.FAIL, result.getCode(), result.getMessage());
        }
        return new FindInheritedPropertiesResult.AppProperties(appId, result.getProperties());
    }

    /**
     * 查找应用继承的全部配置
     */
    public static class FindInheritedPropertiesResult extends AbstractResult {
        // 由近及远继承的所有应用的配置
        private List<AppProperties> appPropertieses = new ArrayList<>();

        public void addAppProperties(AppProperties appProperties) {
            appPropertieses.add(appProperties);
        }

        public List<AppProperties> getAppPropertieses() {
            return appPropertieses;
        }

        /**
         * 应用配置
         */
        public static class AppProperties {
            // 应用id
            private String appId;
            // 配置
            private List<Property> properties;

            public AppProperties(String appId, List<Property> properties) {
                this.appId = appId;
                this.properties = properties;
            }

            public String getAppId() {
                return appId;
            }

            public List<Property> getProperties() {
                return properties;
            }
        }
    }
}
