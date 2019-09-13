/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-11 13:25 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.FindPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindPropertyKeysResult;
import org.antframework.configcenter.facade.vo.Scope;

import java.util.List;

/**
 * 配置key操作类
 */
public final class PropertyKeys {
    // 配置key服务
    private static final PropertyKeyService PROPERTY_KEY_SERVICE = Contexts.getApplicationContext().getBean(PropertyKeyService.class);

    /**
     * 删除配置key
     *
     * @param appId 应用id
     * @param key   key
     */
    public static void deletePropertyKey(String appId, String key) {
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppId(appId);
        order.setKey(key);

        EmptyResult result = PROPERTY_KEY_SERVICE.deletePropertyKey(order);
        FacadeUtils.assertSuccess(result);
    }

    /**
     * 查找配置key集
     *
     * @param appId    应用id
     * @param minScope 最小作用域
     * @return 应用的配置key
     */
    public static List<PropertyKeyInfo> findPropertyKeys(String appId, Scope minScope) {
        FindPropertyKeysOrder order = new FindPropertyKeysOrder();
        order.setAppId(appId);
        order.setMinScope(minScope);

        FindPropertyKeysResult result = PROPERTY_KEY_SERVICE.findPropertyKeys(order);
        FacadeUtils.assertSuccess(result);
        return result.getPropertyKeys();
    }
}
