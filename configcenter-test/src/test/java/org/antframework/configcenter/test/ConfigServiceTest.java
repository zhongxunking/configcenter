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
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindPropertiesResult;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Ignore
public class ConfigServiceTest extends AbstractTest {
    @Autowired
    private ConfigService configService;

    @Test
    public void testFindProperties() {
        FindPropertiesOrder order = new FindPropertiesOrder();
        order.setMainAppId("scbfund");
        order.setQueriedAppId("scbfund");
        order.setProfileId("dev");
        FindPropertiesResult result = configService.findProperties(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindProperties_onlyOutward() {
        FindPropertiesOrder order = new FindPropertiesOrder();
        order.setMainAppId("scbfund");
        order.setQueriedAppId("investment");
        order.setProfileId("dev");
        FindPropertiesResult result = configService.findProperties(order);
        checkResult(result, Status.SUCCESS);
    }
}
