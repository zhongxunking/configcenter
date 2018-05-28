/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 20:53 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindAppSelfPropertiesOrder;
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppSelfPropertiesResult;
import org.antframework.configcenter.facade.result.FindPropertiesResult;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 配置服务单元测试
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

        order = new FindPropertiesOrder();
        order.setMainAppId("investment");
        order.setQueriedAppId("scbfund");
        order.setProfileId("dev");
        result = configService.findProperties(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindAppSelfProperties() {
        FindAppSelfPropertiesOrder order = new FindAppSelfPropertiesOrder();
        order.setAppId("scbfund");
        order.setProfileId("dev");
        order.setMinScope(Scope.PRIVATE);
        FindAppSelfPropertiesResult result = configService.findAppSelfProperties(order);
        checkResult(result, Status.SUCCESS);

        order = new FindAppSelfPropertiesOrder();
        order.setAppId("scbfund");
        order.setProfileId("dev");
        order.setMinScope(Scope.PROTECTED);
        result = configService.findAppSelfProperties(order);
        checkResult(result, Status.SUCCESS);

        order = new FindAppSelfPropertiesOrder();
        order.setAppId("scbfund");
        order.setProfileId("dev");
        order.setMinScope(Scope.PUBLIC);
        result = configService.findAppSelfProperties(order);
        checkResult(result, Status.SUCCESS);
    }
}
