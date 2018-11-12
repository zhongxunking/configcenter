/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-11-11 23:35 创建
 */
package org.antframework.configcenter.web.common;

import org.antframework.manager.facade.info.RelationInfo;
import org.antframework.manager.web.Relations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置key安全等级工具类
 */
public final class KeySecurityLevels {
    // 关系类型
    private static final String RELATION_TYPE = "app-key-securityLevel";

    /**
     * 查找指定应用所有的配置key的安全等级
     *
     * @param appId 应用id
     * @return 配置key的安全等级
     */
    public static Map<String, SecurityLevel> findKeySecurityLevels(String appId) {
        Map<String, SecurityLevel> keyLevels = new HashMap<>();
        List<RelationInfo> relations = Relations.findAllSourceRelations(RELATION_TYPE, appId);
        for (RelationInfo relation : relations) {
            keyLevels.put(relation.getTarget(), SecurityLevel.valueOf(relation.getValue()));
        }
        return keyLevels;
    }
}
