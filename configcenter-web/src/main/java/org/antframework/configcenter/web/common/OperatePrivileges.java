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
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.manager.biz.util.Relations;
import org.antframework.manager.facade.info.RelationInfo;
import org.antframework.manager.web.CurrentManagerAssert;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 操作权限工具类
 */
public final class OperatePrivileges {
    // 关系类型
    private static final String RELATION_TYPE = "appId-keyRegex-privilege";
    // 掩码后的配置value
    private static final String MASKED_VALUE = "******";

    /**
     * 断言提供的配置key对应的权限都是读写权限
     *
     * @param appId 应用id
     * @param keys  配置key
     */
    public static void assertAdminOrOnlyReadWrite(String appId, Set<String> keys) {
        try {
            CurrentManagerAssert.admin();
        } catch (BizException e) {
            List<OperatePrivileges.AppOperatePrivilege> appOperatePrivileges = findInheritedOperatePrivileges(appId);

            Set<String> notReadWriteKeys = new HashSet<>();
            for (String key : keys) {
                OperatePrivilege privilege = calcOperatePrivilege(appOperatePrivileges, key);
                if (privilege != OperatePrivilege.READ_WRITE) {
                    notReadWriteKeys.add(key);
                }
            }

            if (!notReadWriteKeys.isEmpty()) {
                throw new BizException(Status.FAIL, CommonResultCode.UNAUTHORIZED.getCode(), String.format("存在敏感配置%s被修改", ToString.toString(notReadWriteKeys)));
            }
        }
    }

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
     * 对敏感配置进行掩码
     *
     * @param appId      应用id
     * @param properties 需掩码的配置集
     * @return 掩码后的配置集
     */
    public static Set<Property> maskProperties(String appId, Set<Property> properties) {
        List<OperatePrivileges.AppOperatePrivilege> appOperatePrivileges = findInheritedOperatePrivileges(appId);
        Set<Property> maskedProperties = new HashSet<>(properties.size());
        for (Property property : properties) {
            OperatePrivilege privilege = calcOperatePrivilege(appOperatePrivileges, property.getKey());
            if (privilege == OperatePrivilege.NONE) {
                maskedProperties.add(new Property(property.getKey(), MASKED_VALUE, property.getScope()));
            } else {
                maskedProperties.add(property);
            }
        }
        return maskedProperties;
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
