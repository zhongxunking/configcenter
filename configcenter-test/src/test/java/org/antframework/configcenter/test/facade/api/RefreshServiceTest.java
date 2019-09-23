/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 15:12 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.RefreshService;
import org.antframework.configcenter.facade.order.RefreshClientsOrder;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 刷新服务单元测试
 */
@Ignore
public class RefreshServiceTest extends AbstractTest {
    @Autowired
    private RefreshService refreshService;

    @Test
    public void testRefreshClients() {
        String[] appIds = new String[]{null, "customer"};
        String[] profileIds = new String[]{null, "dev"};

        for (String appId : appIds) {
            for (String profileId : profileIds) {
                RefreshClientsOrder order = new RefreshClientsOrder();
                order.setRootAppId(appId);
                order.setRootProfileId(profileId);

                EmptyResult result = refreshService.refreshClients(order);
                checkResult(result, Status.SUCCESS);
            }
        }
    }
}
