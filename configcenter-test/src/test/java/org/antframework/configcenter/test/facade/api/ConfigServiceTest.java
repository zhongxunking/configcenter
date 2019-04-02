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
import org.antframework.configcenter.facade.order.FindAppSelfConfigOrder;
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppSelfConfigResult;
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
        String[] queriedAppIds = new String[]{"customer", "account"};
        String[] profileIds = new String[]{"offline", "dev"};

        for (String queriedId : queriedAppIds) {
            for (String profileId : profileIds) {
                FindPropertiesOrder order = new FindPropertiesOrder();
                order.setMainAppId("customer");
                order.setQueriedAppId(queriedId);
                order.setProfileId(profileId);

                FindPropertiesResult result = configService.findProperties(order);
                checkResult(result, Status.SUCCESS);
            }
        }
    }

    @Test
    public void testFindAppSelfConfig() {
        String[] profileIds = new String[]{"offline", "dev"};
        for (String profileId : profileIds) {
            for (Scope scope : Scope.values()) {
                FindAppSelfConfigOrder order = new FindAppSelfConfigOrder();
                order.setAppId("customer");
                order.setProfileId(profileId);
                order.setMinScope(scope);

                FindAppSelfConfigResult result = configService.findAppSelfConfig(order);
                checkResult(result, Status.SUCCESS);
            }
        }
    }
}
