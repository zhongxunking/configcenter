/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:23 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.*;
import org.antframework.configcenter.biz.util.RefreshClientsUtils;
import org.antframework.configcenter.facade.api.AppService;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.FindAppPropertyKeysOrder;
import org.antframework.configcenter.facade.order.FindInheritedAppsOrder;
import org.antframework.configcenter.facade.result.FindAppPropertyKeysResult;
import org.antframework.configcenter.facade.result.FindInheritedAppsResult;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.manager.web.common.ManagerAssert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性key管理controller
 */
@RestController
@RequestMapping("/manage/propertyKey")
public class PropertyKeyManageController {
    @Autowired
    private PropertyKeyService propertyKeyService;
    @Autowired
    private AppService appService;

    /**
     * 新增或修改属性key
     *
     * @param appId 应用id（必须）
     * @param key   key（必须）
     * @param scope 作用域（必须）
     * @param memo  备注（可选）
     */
    @RequestMapping("/addOrModifyPropertyKey")
    public EmptyResult addOrModifyPropertyKey(String appId, String key, Scope scope, String memo) {
        ManagerAssert.adminOrHaveRelation(appId);
        AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
        order.setAppId(appId);
        order.setKey(key);
        order.setScope(scope);
        order.setMemo(memo);

        EmptyResult result = propertyKeyService.addOrModifyPropertyKey(order);
        // 刷新客户端
        RefreshClientsUtils.refresh(appId, null);
        return result;
    }

    /**
     * 删除属性key
     *
     * @param appId 应用id（必须）
     * @param key   key（必须）
     */
    @RequestMapping("/deletePropertyKey")
    public EmptyResult deletePropertyKey(String appId, String key) {
        ManagerAssert.adminOrHaveRelation(appId);
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppId(appId);
        order.setKey(key);

        EmptyResult result = propertyKeyService.deletePropertyKey(order);
        // 刷新客户端
        RefreshClientsUtils.refresh(appId, null);
        return result;
    }

    /**
     * 查找应用继承的属性key（包含应用自己）
     *
     * @param appId 应用id
     */
    @RequestMapping("/findInheritedPropertyKeys")
    public FindInheritedPropertyKeysResult findInheritedPropertyKeys(String appId) {
        ManagerAssert.adminOrHaveRelation(appId);

        FindInheritedPropertyKeysResult result = new FindInheritedPropertyKeysResult();
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
        for (AppInfo app : getInheritedApp(appId)) {
            result.addAppPropertyKeys(getAppPropertyKeys(app.getAppId(), appId));
        }

        return result;
    }

    // 获取应用继承的所有应用
    private List<AppInfo> getInheritedApp(String appId) {
        FindInheritedAppsOrder order = new FindInheritedAppsOrder();
        order.setAppId(appId);

        FindInheritedAppsResult result = appService.findInheritedApps(order);
        FacadeUtils.assertSuccess(result);
        return result.getInheritedApps();
    }

    // 获取应用的属性key
    private FindInheritedPropertyKeysResult.AppPropertyKeys getAppPropertyKeys(String appId, String mainAppId) {
        Scope minScope = Scope.PRIVATE;
        if (!StringUtils.equals(appId, mainAppId)) {
            minScope = Scope.PROTECTED;
        }

        FindAppPropertyKeysOrder order = new FindAppPropertyKeysOrder();
        order.setAppId(appId);
        order.setMinScope(minScope);

        FindAppPropertyKeysResult result = propertyKeyService.findAppPropertyKeys(order);
        FacadeUtils.assertSuccess(result);
        return new FindInheritedPropertyKeysResult.AppPropertyKeys(appId, result.getPropertyKeys());
    }

    /**
     * 查找应用继承的所有应用的属性key
     */
    public static class FindInheritedPropertyKeysResult extends AbstractResult {
        // 由近及远继承的所用应用的属性key
        private List<AppPropertyKeys> appPropertyKeyses = new ArrayList<>();

        public void addAppPropertyKeys(AppPropertyKeys appPropertyKeys) {
            appPropertyKeyses.add(appPropertyKeys);
        }

        public List<AppPropertyKeys> getAppPropertyKeyses() {
            return appPropertyKeyses;
        }

        /**
         * 应用属性key
         */
        public static class AppPropertyKeys {
            // 应用id
            private String appId;
            // 属性key
            private List<PropertyKeyInfo> propertyKeys;

            public AppPropertyKeys(String appId, List<PropertyKeyInfo> propertyKeys) {
                this.appId = appId;
                this.propertyKeys = propertyKeys;
            }

            public String getAppId() {
                return appId;
            }

            public List<PropertyKeyInfo> getPropertyKeys() {
                return propertyKeys;
            }
        }
    }
}
