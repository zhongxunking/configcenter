/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 16:34 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.api.manage.RefreshService;
import org.antframework.configcenter.facade.order.manage.SyncDataToZkOrder;
import org.antframework.configcenter.facade.order.manage.TriggerClientRefreshOrder;
import org.antframework.configcenter.facade.result.manage.SyncDataToZkResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 刷新controller
 */
@RestController
@RequestMapping("/refresh")
public class RefreshController {
    @Autowired
    private RefreshService refreshService;

    /**
     * 先同步所有应用和环境到zookeeper，然后触发对应客户端刷新配置
     *
     * @param appCode     应用编码（不传表示刷新所有应用）
     * @param profileCode 环境编码（不传表示刷新所有环境）
     */
    @RequestMapping("/refreshZkAndClient")
    public AbstractResult refreshZkAndClient(String appCode, String profileCode) {
        // 同步数据到zookeeper
        SyncDataToZkResult syncDataToZkResult = refreshService.syncDataToZk(new SyncDataToZkOrder());
        if (!syncDataToZkResult.isSuccess()) {
            return syncDataToZkResult;
        }
        // 触发客户端刷新配置
        TriggerClientRefreshOrder order = new TriggerClientRefreshOrder();
        order.setAppCode(appCode);
        order.setProfileCode(profileCode);
        return refreshService.triggerClientRefresh(order);
    }
}
