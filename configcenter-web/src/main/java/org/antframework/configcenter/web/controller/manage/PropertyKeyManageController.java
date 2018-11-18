/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:23 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.PropertyKeyUtils;
import org.antframework.configcenter.biz.util.RefreshUtils;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.KeyPrivileges;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.Privilege;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 配置key管理controller
 */
@RestController
@RequestMapping("/manage/propertyKey")
public class PropertyKeyManageController {
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

        EmptyResult result = propertyKeyService.addOrModifyPropertyKey(order);
        // 刷新客户端
        RefreshUtils.refreshClients(appId, null);
        return result;
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
        // 刷新客户端
        RefreshUtils.refreshClients(appId, null);
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
            result.addAppPropertyKey(getAppPropertyKey(app.getAppId(), appId));
        }

        return result;
    }

    // 获取应用的配置key
    private FindInheritedPropertyKeysResult.AppPropertyKey getAppPropertyKey(String appId, String mainAppId) {
        Scope minScope = Scope.PRIVATE;
        if (!StringUtils.equals(appId, mainAppId)) {
            minScope = Scope.PROTECTED;
        }

        return new FindInheritedPropertyKeysResult.AppPropertyKey(appId, PropertyKeyUtils.findAppPropertyKeys(appId, minScope));
    }

    /**
     * 查找指定应用所有的配置key的权限
     *
     * @param appId 应用id（必须）
     * @return 配置key的权限
     */
    @RequestMapping("/findKeyPrivileges")
    public FindKeyPrivilegesResult findKeyPrivileges(String appId) {
        FindKeyPrivilegesResult result = new FindKeyPrivilegesResult();
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());

        Map<String, Privilege> keyPrivileges = KeyPrivileges.findPrivileges(appId);
        result.setKeyPrivileges(keyPrivileges);

        return result;
    }

    /**
     * 查找应用继承的所有应用的配置key
     */
    public static class FindInheritedPropertyKeysResult extends AbstractResult {
        // 由近及远继承的所用应用的配置key
        private List<AppPropertyKey> appPropertyKeys = new ArrayList<>();

        public void addAppPropertyKey(AppPropertyKey appPropertyKey) {
            appPropertyKeys.add(appPropertyKey);
        }

        public List<AppPropertyKey> getAppPropertyKeys() {
            return appPropertyKeys;
        }

        /**
         * 应用的配置key
         */
        public static class AppPropertyKey implements Serializable {
            // 应用id
            private String appId;
            // 配置key
            private List<PropertyKeyInfo> propertyKeys;

            public AppPropertyKey(String appId, List<PropertyKeyInfo> propertyKeys) {
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

    /**
     * 查找指定应用所有的配置key的权限result
     */
    public static class FindKeyPrivilegesResult extends AbstractResult {
        // key对应的权限
        private Map<String, Privilege> keyPrivileges;

        public Map<String, Privilege> getKeyPrivileges() {
            return keyPrivileges;
        }

        public void setKeyPrivileges(Map<String, Privilege> keyPrivileges) {
            this.keyPrivileges = keyPrivileges;
        }
    }
}
