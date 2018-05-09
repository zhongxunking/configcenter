/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 15:12 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.RefreshService;
import org.antframework.configcenter.facade.order.TriggerClientRefreshOrder;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Ignore
public class RefreshServiceTest extends AbstractTest {
    @Autowired
    private RefreshService refreshService;

    @Test
    public void testSyncDataToZk() {
        EmptyResult result = refreshService.syncDataToZk(new EmptyOrder());
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testTriggerClientRefresh() {
        TriggerClientRefreshOrder order = new TriggerClientRefreshOrder();
        order.setAppId("scbfund");
        order.setProfileId("dev");
        EmptyResult result = refreshService.triggerClientRefresh(order);
        checkResult(result, Status.SUCCESS);

        order = new TriggerClientRefreshOrder();
        order.setAppId("scbfund");
        order.setProfileId(null);
        result = refreshService.triggerClientRefresh(order);
        checkResult(result, Status.SUCCESS);

        order = new TriggerClientRefreshOrder();
        order.setAppId(null);
        order.setProfileId("dev");
        result = refreshService.triggerClientRefresh(order);
        checkResult(result, Status.SUCCESS);

        order = new TriggerClientRefreshOrder();
        order.setAppId(null);
        order.setProfileId(null);
        result = refreshService.triggerClientRefresh(order);
        checkResult(result, Status.SUCCESS);
    }
}
