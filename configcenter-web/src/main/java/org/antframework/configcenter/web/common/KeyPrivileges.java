/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-11-17 21:59 创建
 */
package org.antframework.configcenter.web.common;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.PropertyKeyUtils;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.facade.info.ManagerInfo;
import org.antframework.manager.facade.info.RelationInfo;
import org.antframework.manager.web.Managers;
import org.antframework.manager.web.Relations;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 配置key的权限工具类
 */
public final class KeyPrivileges {
    // 关系类型
    private static final String RELATION_TYPE = "app-key-privilege";

    /**
     * 查找权限
     *
     * @param appId 应用id
     * @param key   配置key
     * @return 配置key的权限
     */
    public static Privilege findPrivilege(String appId, String key) {
        Map<String, Privilege> keyPrivileges = findPrivileges(appId);
        return keyPrivileges.getOrDefault(key, Privilege.READ_WRITE);
    }

    /**
     * 查找应用及其继承的所有的配置key的权限
     *
     * @param appId 应用id
     * @return 配置key的权限
     */
    public static Map<String, Privilege> findPrivileges(String appId) {
        Map<String, Privilege> keyPrivileges = new HashMap<>();

        for (AppInfo app : AppUtils.findInheritedApps(appId)) {
            Map<String, Privilege> temp = new HashMap<>();
            for (RelationInfo relation : Relations.findAllSourceRelations(RELATION_TYPE, app.getAppId())) {
                temp.put(relation.getTarget(), Privilege.valueOf(relation.getValue()));
            }
            Scope minScope = Objects.equals(app.getAppId(), appId) ? Scope.PRIVATE : Scope.PROTECTED;
            for (PropertyKeyInfo propertyKey : PropertyKeyUtils.findAppPropertyKeys(app.getAppId(), minScope)) {
                temp.putIfAbsent(propertyKey.getKey(), Privilege.READ_WRITE);
            }

            temp.putAll(keyPrivileges);
            keyPrivileges = temp;
        }

        return keyPrivileges;
    }

    /**
     * 删除配置key的权限
     *
     * @param appId 应用id
     * @param key   配置key
     */
    public static void deletePrivilege(String appId, String key) {
        Relations.deleteRelations(RELATION_TYPE, appId, key);
    }

    /**
     * 断言当前管理员为超级管理员或指定配置key的权限是读写权限
     *
     * @param appId 应用id
     * @param key   配置key
     */
    public static void adminOrReadWrite(String appId, String key) {
        ManagerInfo manager = Managers.currentManager();
        if (manager.getType() == ManagerType.ADMIN) {
            return;
        }
        if (findPrivilege(appId, key) != Privilege.READ_WRITE) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("无权限操作敏感配置key[%s]", key));
        }
    }
}
