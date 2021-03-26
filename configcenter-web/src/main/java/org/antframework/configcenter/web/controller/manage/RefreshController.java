/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 16:34 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.api.RefreshService;
import org.antframework.configcenter.facade.order.RefreshClientsOrder;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.manager.web.CurrentManagerAssert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 刷新controller
 */
@RestController
@RequestMapping("/manage/refresh")
@AllArgsConstructor
public class RefreshController {
    // 刷新服务
    private final RefreshService refreshService;

    /**
     * 刷新客户端
     *
     * @param rootAppId     根应用id（不传表示刷新所有应用）
     * @param rootProfileId 根环境id（不传表示刷新所有环境）
     */
    @RequestMapping("/refreshClients")
    public EmptyResult refreshClients(String rootAppId, String rootProfileId) {
        if (rootAppId == null) {
            CurrentManagerAssert.admin();
        } else {
            ManagerApps.assertAdminOrHaveApp(rootAppId);
        }
        RefreshClientsOrder order = new RefreshClientsOrder();
        order.setRootAppId(rootAppId);
        order.setRootProfileId(rootProfileId);

        return refreshService.refreshClients(order);
    }
}
