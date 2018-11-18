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
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 配置key的权限工具类
 */
public final class KeyPrivileges {
    // 关系类型
    private static final String RELATION_TYPE = "app-key-privilege";

    /**
     * 查找应用所有的配置key的权限
     *
     * @param appId 应用id
     * @return 配置key的权限
     */
    public static Map<String, Privilege> findPrivileges(String appId) {
        Map<String, Privilege> keyPrivileges = new HashMap<>();
        List<RelationInfo> relations = Relations.findAllSourceRelations(RELATION_TYPE, appId);
        for (RelationInfo relation : relations) {
            keyPrivileges.put(relation.getTarget(), Privilege.valueOf(relation.getValue()));
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
        // 根据应用继承关系，一级一级判断是否拥有权限
        for (AppInfo app : AppUtils.findInheritedApps(appId)) {
            Map<String, Privilege> keyPrivileges = findPrivileges(app.getAppId());

            Scope minScope = Objects.equals(app.getAppId(), appId) ? Scope.PRIVATE : Scope.PROTECTED;
            List<PropertyKeyInfo> propertyKeys = PropertyKeyUtils.findAppPropertyKeys(app.getAppId(), minScope);
            for (PropertyKeyInfo propertyKey : propertyKeys) {
                if (!Objects.equals(propertyKey.getKey(), key)) {
                    continue;
                }
                Privilege privilege = keyPrivileges.getOrDefault(key, Privilege.READ_WRITE);
                if (privilege == Privilege.READ_WRITE) {
                    return;
                } else {
                    throw new BizException(Status.FAIL,
                            CommonResultCode.ILLEGAL_STATE.getCode(),
                            String.format("无法新建或修改配置key[%s]，因为应用[%s]指定该key为敏感配置。" +
                                    "如果确定需要添加或修改该key，请让超级管理员进行操作", key, app.getAppId()));
                }
            }
        }
    }
}
