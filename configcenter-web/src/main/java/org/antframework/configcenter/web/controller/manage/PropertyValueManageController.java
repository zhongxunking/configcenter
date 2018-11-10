/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:38 创建
 */
package org.antframework.configcenter.web.controller.manage;

import com.alibaba.fastjson.JSON;
import org.antframework.common.util.facade.*;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.ConfigUtils;
import org.antframework.configcenter.biz.util.RefreshUtils;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.ProfileProperty;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.ManagerApps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 属性value管理controller
 */
@RestController
@RequestMapping("/manage/propertyValue")
public class PropertyValueManageController {
    @Autowired
    private PropertyValueService propertyValueService;

    /**
     * 设置多个属性value
     *
     * @param appId     应用id（必须）
     * @param profileId 环境id（必须）
     * @param keys      一个或多个key（必须，json格式）
     * @param values    与keys一一对应的values（必须，json格式）
     */
    @RequestMapping("/setPropertyValues")
    public EmptyResult setPropertyValues(String appId, String profileId, String keys, String values) {
        ManagerApps.adminOrHaveApp(appId);

        List<String> parsedKeys = JSON.parseArray(keys, String.class);
        List<String> parsedValues = JSON.parseArray(values, String.class);
        if (parsedKeys.size() != parsedValues.size()) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "属性key和value数量不相等");
        }

        SetPropertyValuesOrder order = new SetPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        for (int i = 0; i < parsedKeys.size(); i++) {
            SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
            keyValue.setKey(parsedKeys.get(i));
            keyValue.setValue(parsedValues.get(i));
            order.addKeyValue(keyValue);
        }

        EmptyResult result = propertyValueService.setPropertyValues(order);
        // 刷新客户端
        RefreshUtils.refreshClients(appId, profileId);
        return result;
    }

    /**
     * 查找应用继承的配置
     *
     * @param appId     应用id
     * @param profileId 环境id
     */
    @RequestMapping("/findInheritedProperties")
    public FindInheritedPropertiesResult findInheritedProperties(String appId, String profileId) {
        ManagerApps.adminOrHaveApp(appId);

        FindInheritedPropertiesResult result = new FindInheritedPropertiesResult();
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
        // 获取继承的所有应用的配置
        for (AppInfo app : AppUtils.findInheritedApps(appId)) {
            // 获取应用的配置
            Scope minScope = Objects.equals(app.getAppId(), appId) ? Scope.PRIVATE : Scope.PROTECTED;
            List<ProfileProperty> profileProperties = ConfigUtils.findAppSelfProperties(app.getAppId(), profileId, minScope);
            result.addAppProperty(new FindInheritedPropertiesResult.AppProperty(app.getAppId(), profileProperties));
        }
        return result;
    }

    /**
     * 查找应用继承的全部配置
     */
    public static class FindInheritedPropertiesResult extends AbstractResult {
        // 由近及远继承的所有应用的配置
        private List<AppProperty> appProperties = new ArrayList<>();

        public List<AppProperty> getAppProperties() {
            return appProperties;
        }

        public void addAppProperty(AppProperty appProperty) {
            appProperties.add(appProperty);
        }

        /**
         * 应用配置
         */
        public static class AppProperty implements Serializable {
            // 应用id
            private String appId;
            // 由近及远继承的所用环境中的配置
            private List<ProfileProperty> profileProperties;

            public AppProperty(String appId, List<ProfileProperty> profileProperties) {
                this.appId = appId;
                this.profileProperties = profileProperties;
            }

            public String getAppId() {
                return appId;
            }

            public List<ProfileProperty> getProfileProperties() {
                return profileProperties;
            }
        }
    }
}
