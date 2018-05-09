/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 15:07 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.AppService;
import org.antframework.configcenter.facade.order.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.order.DeleteAppOrder;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.order.QueryAppOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.QueryAppResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Ignore
public class AppServiceTest extends AbstractTest {
    @Autowired
    private AppService appService;

    @Test
    public void testAddOrModifyApp() {
        AddOrModifyAppOrder order = new AddOrModifyAppOrder();
        order.setAppId("scbfund");
        order.setMemo("升财宝");
        EmptyResult result = appService.addOrModifyApp(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testDeleteApp() {
        DeleteAppOrder order = new DeleteAppOrder();
        order.setAppId("scbfund");
        EmptyResult result = appService.deleteApp(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindApp() {
        FindAppOrder order = new FindAppOrder();
        order.setAppId("scbfund");
        FindAppResult result = appService.findApp(order);
        checkResult(result, Status.SUCCESS);
        Assert.assertEquals("scbfund", result.getAppInfo().getAppId());
    }

    @Test
    public void testQueryApp() {
        QueryAppOrder order = new QueryAppOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        QueryAppResult result = appService.queryApp(order);
        checkResult(result, Status.SUCCESS);
    }
}
