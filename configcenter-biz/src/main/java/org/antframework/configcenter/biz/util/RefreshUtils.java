/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-11 15:29 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.configcenter.facade.api.RefreshService;
import org.antframework.configcenter.facade.order.TriggerClientsRefreshOrder;

/**
 * 刷新工具类
 */
public final class RefreshUtils {
    // 刷新服务
    private static final RefreshService REFRESH_SERVICE = Contexts.getApplicationContext().getBean(RefreshService.class);

    /**
     * 同步数据到zookeeper
     */
    public static void syncDataToZk() {
        REFRESH_SERVICE.syncDataToZk(new EmptyOrder());
    }

    /**
     * 触发客户端刷新
     *
     * @param appId     应用id（null表示刷新所有应用）
     * @param profileId 环境id（null表示刷新所有环境）
     */
    public static void triggerClientsRefresh(String appId, String profileId) {
        TriggerClientsRefreshOrder order = new TriggerClientsRefreshOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        REFRESH_SERVICE.triggerClientsRefresh(order);
    }
}
