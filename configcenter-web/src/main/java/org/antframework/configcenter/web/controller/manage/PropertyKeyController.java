/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:23 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.PropertyKeyUtils;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.KeyPrivileges;
import org.antframework.configcenter.web.common.ManagerApps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 配置key管理controller
 */
@RestController
@RequestMapping("/manage/propertyKey")
public class PropertyKeyController {
    @Autowired
    private PropertyKeyService propertyKeyService;

    /**
     * 新增或修改配置key
     *
     * @param appId 应用id（必须）
     * @param key   key（必须）
     * @param scope 作用域（必须）
     * @param memo  备注（可选）
     */
    @RequestMapping("/addOrModifyPropertyKey")
    public EmptyResult addOrModifyPropertyKey(String appId, String key, Scope scope, String memo) {
        ManagerApps.adminOrHaveApp(appId);
        KeyPrivileges.adminOrReadWrite(appId, key);
        AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
        order.setAppId(appId);
        order.setKey(key);
        order.setScope(scope);
        order.setMemo(memo);

        return propertyKeyService.addOrModifyPropertyKey(order);
    }

    /**
     * 删除配置key
     *
     * @param appId 应用id（必须）
     * @param key   key（必须）
     */
    @RequestMapping("/deletePropertyKey")
    public EmptyResult deletePropertyKey(String appId, String key) {
        ManagerApps.adminOrHaveApp(appId);
        KeyPrivileges.adminOrReadWrite(appId, key);
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppId(appId);
        order.setKey(key);

        EmptyResult result = propertyKeyService.deletePropertyKey(order);
        if (result.isSuccess()) {
            KeyPrivileges.deletePrivilege(appId, key);
        }
        return result;
    }

    /**
     * 查找应用继承的配置key（包含应用自己）
     *
     * @param appId 应用id（必须）
     */
    @RequestMapping("/findInheritedPropertyKeys")
    public FindInheritedPropertyKeysResult findInheritedPropertyKeys(String appId) {
        ManagerApps.adminOrHaveApp(appId);

        FindInheritedPropertyKeysResult result = new FindInheritedPropertyKeysResult();
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
        for (AppInfo app : AppUtils.findInheritedApps(appId)) {
            Scope minScope = Objects.equals(app.getAppId(), appId) ? Scope.PRIVATE : Scope.PROTECTED;
            List<PropertyKeyInfo> propertyKeys = PropertyKeyUtils.findAppPropertyKeys(app.getAppId(), minScope);
            result.addAppPropertyKey(new FindInheritedPropertyKeysResult.AppPropertyKey(app, propertyKeys));
        }

        return result;
    }

    /**
     * 查找应用继承的配置权限
     *
     * @param appId 应用id（必须）
     * @return 继承的配置权限
     */
    @RequestMapping("/findInheritedPrivileges")
    public FindInheritedPrivilegesResult findInheritedPrivileges(String appId) {
        FindInheritedPrivilegesResult result = new FindInheritedPrivilegesResult();
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
        result.setAppPrivileges(KeyPrivileges.findInheritedPrivileges(appId));

        return result;
    }

    /**
     * 查找应用继承的所有应用的配置key
     */
    @Getter
    public static class FindInheritedPropertyKeysResult extends AbstractResult {
        // 由近及远继承的所用应用的配置key
        private final List<AppPropertyKey> appPropertyKeys = new ArrayList<>();

        public void addAppPropertyKey(AppPropertyKey appPropertyKey) {
            appPropertyKeys.add(appPropertyKey);
        }

        /**
         * 应用的配置key
         */
        @AllArgsConstructor
        @Getter
        public static final class AppPropertyKey implements Serializable {
            // 应用
            private final AppInfo app;
            // 所有的配置key
            private final List<PropertyKeyInfo> propertyKeys;

            @Override
            public String toString() {
                return ToString.toString(this);
            }
        }
    }

    /**
     * 查找继承的配置权限result
     */
    @Getter
    @Setter
    public static class FindInheritedPrivilegesResult extends AbstractResult {
        // 由近及远应用继承的配置权限（该应用本身在第一位）
        private List<KeyPrivileges.AppPrivilege> appPrivileges;
    }
}
