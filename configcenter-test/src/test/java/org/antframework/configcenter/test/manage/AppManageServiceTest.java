/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 15:07 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.manage.AppManageService;
import org.antframework.configcenter.facade.order.manage.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.order.manage.DeleteAppOrder;
import org.antframework.configcenter.facade.order.manage.QueryAppOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyAppResult;
import org.antframework.configcenter.facade.result.manage.DeleteAppResult;
import org.antframework.configcenter.facade.result.manage.QueryAppResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class AppManageServiceTest extends AbstractTest {
    @Autowired
    private AppManageService appManageService;

    @Test
    public void testAddOrModifyApp() {
        AddOrModifyAppOrder order = new AddOrModifyAppOrder();
        order.setAppCode("scbfund");
        order.setMemo("升财宝");
        AddOrModifyAppResult result = appManageService.addOrModifyApp(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testDeleteApp() {
        DeleteAppOrder order = new DeleteAppOrder();
        order.setAppCode("scbfund");
        DeleteAppResult result = appManageService.deleteApp(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryApp() {
        testAddOrModifyApp();
        QueryAppOrder order = new QueryAppOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        order.setAppCode("scbfu");
        QueryAppResult result = appManageService.queryApp(order);
        checkResult(result, Status.SUCCESS);
        Assert.assertEquals(1, result.getInfos().size());
    }
}
