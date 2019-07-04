/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-25 23:44 创建
 */
package org.antframework.configcenter.web.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.manager.biz.util.Relations;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.facade.info.ManagerInfo;
import org.antframework.manager.facade.info.RelationInfo;
import org.antframework.manager.web.CurrentManagers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 操作权限工具类
 */
public final class OperatePrivileges {
    // 关系类型
    private static final String RELATION_TYPE = "appId-keyRegex-privilege";

    /**
     * 添加或修改操作权限
     *
     * @param appId     应用id
     * @param keyRegex  key正则表达式
     * @param privilege 操作权限
     */
    public static void addOrModifyOperatePrivilege(String appId, String keyRegex, OperatePrivilege privilege) {
        Relations.addOrModifyRelation(RELATION_TYPE, appId, keyRegex, privilege.name());
    }

    /**
     * 删除操作权限
     *
     * @param appId    应用id
     * @param keyRegex 配置key正则表达式（null表示删除该应用的所有权限）
     */
    public static void deleteOperatePrivileges(String appId, String keyRegex) {
        Relations.deleteRelations(RELATION_TYPE, appId, keyRegex);
    }

    /**
     * 断言当前管理员为超级管理员或指定配置key的权限是读写权限
     *
     * @param appId 应用id
     * @param key   配置key
     */
    public static void adminOrReadWrite(String appId, String key) {
        ManagerInfo manager = CurrentManagers.current();
        if (manager.getType() == ManagerType.ADMIN) {
            return;
        }
        OperatePrivilege privilege = calcOperatePrivilege(findInheritedOperatePrivileges(appId), key);
        if (privilege != OperatePrivilege.READ_WRITE) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("无权限操作敏感配置key[%s]", key));
        }
    }

    /**
     * 计算操作权限
     *
     * @param appOperatePrivileges 应用继承的操作权限
     * @param key                  配置key
     * @return 配置key的操作权限
     */
    public static OperatePrivilege calcOperatePrivilege(List<AppOperatePrivilege> appOperatePrivileges, String key) {
        for (AppOperatePrivilege appOperatePrivilege : appOperatePrivileges) {
            for (Map.Entry<String, OperatePrivilege> entry : appOperatePrivilege.getKeyRegexPrivileges().entrySet()) {
                if (Pattern.matches(entry.getKey(), key)) {
                    return entry.getValue();
                }
            }
        }
        return OperatePrivilege.READ_WRITE;
    }

    /**
     * 查找应用继承的操作权限
     *
     * @param appId 应用id
     * @return 由近及远应用继承的操作权限（该应用本身在第一位）
     */
    public static List<AppOperatePrivilege> findInheritedOperatePrivileges(String appId) {
        List<AppOperatePrivilege> appOperatePrivileges = new ArrayList<>();
        for (AppInfo app : Apps.findInheritedApps(appId)) {
            Map<String, OperatePrivilege> keyRegexPrivileges = new HashMap<>();
            for (RelationInfo relation : Relations.findAllSourceRelations(RELATION_TYPE, app.getAppId())) {
                keyRegexPrivileges.put(relation.getTarget(), OperatePrivilege.valueOf(relation.getValue()));
            }
            appOperatePrivileges.add(new AppOperatePrivilege(app, keyRegexPrivileges));
        }
        return appOperatePrivileges;
    }

    /**
     * 应用的操作权限
     */
    @AllArgsConstructor
    @Getter
    public static final class AppOperatePrivilege implements Serializable {
        // 应用
        private final AppInfo app;
        // 配置key正则表达式以及对应的操作权限
        private final Map<String, OperatePrivilege> keyRegexPrivileges;

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }
}
