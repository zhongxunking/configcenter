/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-18 00:28 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.RefreshService;
import org.antframework.configcenter.facade.order.TriggerClientsRefreshOrder;

/**
 * 刷新客户端工具类
 */
public class RefreshClientsUtils {
    // 刷新服务
    private static final RefreshService REFRESH_SERVICE = Contexts.getApplicationContext().getBean(RefreshService.class);

    /**
     * 刷新客户端
     *
     * @param appId     应用id（null表示刷新所有应用）
     * @param profileId 环境id（null表示刷新所有环境）
     */
    public static void refresh(String appId, String profileId) {
        TriggerClientsRefreshOrder order = new TriggerClientsRefreshOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        EmptyResult result = REFRESH_SERVICE.triggerClientsRefresh(order);
        FacadeUtils.assertSuccess(result);
    }
}
