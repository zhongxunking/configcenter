/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 15:12 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.manage.RefreshService;
import org.antframework.configcenter.facade.order.manage.SyncDataToZkOrder;
import org.antframework.configcenter.facade.result.manage.SyncDataToZkResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class RefreshServiceTest extends AbstractTest {
    @Autowired
    private RefreshService refreshService;

    @Test
    public void testSyncDataToZk() {
        SyncDataToZkResult result = refreshService.syncDataToZk(new SyncDataToZkOrder());
        checkResult(result, Status.SUCCESS);
    }
}
