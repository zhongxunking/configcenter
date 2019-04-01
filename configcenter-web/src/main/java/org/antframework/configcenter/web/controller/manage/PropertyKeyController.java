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
import org.antframework.common.util.facade.*;
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
import org.antframework.configcenter.web.common.Privilege;
import org.antframework.manager.web.Managers;
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
            KeyPrivileges.deletePrivileges(appId, key);
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

        FindInheritedPropertyKeysResult result = FacadeUtils.buildSuccess(FindInheritedPropertyKeysResult.class);
        for (AppInfo app : AppUtils.findInheritedApps(appId)) {
            List<PropertyKeyInfo> propertyKeys = PropertyKeyUtils.findAppPropertyKeys(app.getAppId(), Scope.PRIVATE);
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
        ManagerApps.adminOrHaveApp(appId);

        FindInheritedPrivilegesResult result = FacadeUtils.buildSuccess(FindInheritedPrivilegesResult.class);
        result.setAppPrivileges(KeyPrivileges.findInheritedPrivileges(appId));
        return result;
    }

    /**
     * 设置配置key的权限
     *
     * @param appId     应用id（必须）
     * @param key       配置key（必须）
     * @param privilege 权限（必须）
     */
    @RequestMapping("/setKeyPrivilege")
    public EmptyResult setKeyPrivilege(String appId, String key, Privilege privilege) {
        Managers.admin();
        assertExistingKey(appId, key);
        // 设置权限
        KeyPrivileges.setPrivilege(appId, key, privilege);

        return FacadeUtils.buildSuccess(EmptyResult.class);
    }

    // 断言存在配置key
    private void assertExistingKey(String appId, String key) {
        List<PropertyKeyInfo> propertyKeys = PropertyKeyUtils.findAppPropertyKeys(appId, Scope.PRIVATE);
        for (PropertyKeyInfo propertyKey : propertyKeys) {
            if (Objects.equals(propertyKey.getKey(), key)) {
                return;
            }
        }
        throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在配置key[%s]", appId, key));
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
