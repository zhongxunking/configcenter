/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-11-14 21:57 创建
 */
package org.antframework.configcenter.web.common;

import org.antframework.manager.facade.info.RelationInfo;
import org.antframework.manager.web.Relations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置key的可操作范围工具类
 */
public class KeyOperationScopes {
    // 关系类型
    private static final String RELATION_TYPE = "app-key-operationScope";

    /**
     * 查找指定应用所有的配置key的可操作范围
     *
     * @param appId 应用id
     * @return 配置key的可操作范围
     */
    public static Map<String, KeyOperationScope> findKeyOperationScopes(String appId) {
        Map<String, KeyOperationScope> scopeMap = new HashMap<>();
        List<RelationInfo> relations = Relations.findAllSourceRelations(RELATION_TYPE, appId);
        for (RelationInfo relation : relations) {
            scopeMap.put(relation.getTarget(), KeyOperationScope.valueOf(relation.getValue()));
        }
        return scopeMap;
    }
}
