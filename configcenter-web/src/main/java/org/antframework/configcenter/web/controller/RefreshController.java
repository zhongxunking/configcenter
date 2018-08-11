/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 16:34 创建
 */
package org.antframework.configcenter.web.controller;

import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.RefreshService;
import org.antframework.configcenter.facade.order.TriggerClientsRefreshOrder;
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
     * @param appId     应用id（不传表示刷新所有应用）
     * @param profileId 环境id（不传表示刷新所有环境）
     */
    @RequestMapping("/refreshZkAndClients")
    public EmptyResult refreshZkAndClients(String appId, String profileId) {
        // 同步数据到zookeeper
        EmptyResult result = refreshService.syncDataToZk(new EmptyOrder());
        FacadeUtils.assertSuccess(result);
        // 触发客户端刷新配置
        TriggerClientsRefreshOrder order = new TriggerClientsRefreshOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        result = refreshService.triggerClientsRefresh(order);

        return result;
    }
}
