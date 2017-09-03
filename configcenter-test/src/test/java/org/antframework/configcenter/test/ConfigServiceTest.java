/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 20:53 创建
 */
package org.antframework.configcenter.test;

import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.order.QueryAppProfilePropertyOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.QueryAppProfilePropertyResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class ConfigServiceTest extends AbstractTest {
    @Autowired
    private ConfigService configService;

    @Test
    public void testFindApp() {
        FindAppOrder order = new FindAppOrder();
        order.setAppCode("scbfund");
        FindAppResult result = configService.findApp(order);
        checkResult(result, Status.SUCCESS);
        Assert.assertEquals("scbfund", result.getAppInfo().getAppCode());
    }

    @Test
    public void testQueryAppProfileProperty() {
        QueryAppProfilePropertyOrder order = new QueryAppProfilePropertyOrder();
        order.setAppCode("scbfund");
        order.setProfileCode("dev");
        order.setOnlyCommon(false);
        QueryAppProfilePropertyResult result = configService.queryAppProfileProperty(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryAppProfileProperty_onlyCommon() {
        QueryAppProfilePropertyOrder order = new QueryAppProfilePropertyOrder();
        order.setAppCode("scbfund");
        order.setProfileCode("dev");
        order.setOnlyCommon(true);
        QueryAppProfilePropertyResult result = configService.queryAppProfileProperty(order);
        checkResult(result, Status.SUCCESS);
    }
}
