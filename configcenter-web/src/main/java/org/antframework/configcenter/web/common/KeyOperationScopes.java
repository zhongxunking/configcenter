/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-11-14 21:57 创建
 */
package org.antframework.configcenter.web.common;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.manager.facade.info.RelationInfo;
import org.antframework.manager.web.Managers;
import org.antframework.manager.web.Relations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置key的可操作范围工具类
 */
public final class KeyOperationScopes {
    // 关系类型
    private static final String RELATION_TYPE = "app-key-operationScope";

    /**
     * 断言当前管理员为超级管理员或指定配置key操作范围是读写
     *
     * @param appId 应用id
     * @param key   配置key
     */
    public static void adminOrReadWrite(String appId, String key) {
        try {
            Managers.admin();
        } catch (BizException e) {
            Managers.currentManager();
            Map<String, OperationScope> scopeMap = findKeyOperationScopes(appId);
            OperationScope scope = scopeMap.getOrDefault(key, OperationScope.READ_WRITE);
            if (scope != OperationScope.READ_WRITE) {
                throw new BizException(Status.FAIL, CommonResultCode.UNAUTHORIZED.getCode(), CommonResultCode.UNAUTHORIZED.getMessage());
            }
        }
    }

    /**
     * 查找指定应用所有的配置key的可操作范围
     *
     * @param appId 应用id
     * @return 配置key的可操作范围
     */
    public static Map<String, OperationScope> findKeyOperationScopes(String appId) {
        Map<String, OperationScope> scopeMap = new HashMap<>();
        List<RelationInfo> relations = Relations.findAllSourceRelations(RELATION_TYPE, appId);
        for (RelationInfo relation : relations) {
            scopeMap.put(relation.getTarget(), OperationScope.valueOf(relation.getValue()));
        }
        return scopeMap;
    }

    /**
     * 删除配置key的可操作范围
     *
     * @param appId 应用id
     * @param key   配置key
     */
    public static void deleteKeyOperationScope(String appId, String key) {
        Relations.deleteRelations(RELATION_TYPE, appId, key);
    }
}
