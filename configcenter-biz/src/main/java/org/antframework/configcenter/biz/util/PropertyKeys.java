/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-11 13:25 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.FindAppPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindAppPropertyKeysResult;
import org.antframework.configcenter.facade.vo.Scope;

import java.util.List;

/**
 * 配置key操作类
 */
public final class PropertyKeys {
    // 配置key服务
    private static final PropertyKeyService PROPERTY_KEY_SERVICE = Contexts.getApplicationContext().getBean(PropertyKeyService.class);

    /**
     * 查找应用的配置key
     *
     * @param appId    应用id
     * @param minScope 最小作用域
     * @return 应用的配置key
     */
    public static List<PropertyKeyInfo> findAppPropertyKeys(String appId, Scope minScope) {
        FindAppPropertyKeysOrder order = new FindAppPropertyKeysOrder();
        order.setAppId(appId);
        order.setMinScope(minScope);

        FindAppPropertyKeysResult result = PROPERTY_KEY_SERVICE.findAppPropertyKeys(order);
        FacadeUtils.assertSuccess(result);
        return result.getPropertyKeys();
    }
}
