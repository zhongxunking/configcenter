/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 20:53 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindConfigOrder;
import org.antframework.configcenter.facade.order.FindInheritedAppReleasesOrder;
import org.antframework.configcenter.facade.result.FindConfigResult;
import org.antframework.configcenter.facade.result.FindInheritedAppReleasesResult;
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
    public void testFindConfig() {
        String[] queriedAppIds = new String[]{"customer", "account"};
        String[] profileIds = new String[]{"offline", "dev"};

        for (String queriedId : queriedAppIds) {
            for (String profileId : profileIds) {
                FindConfigOrder order = new FindConfigOrder();
                order.setMainAppId("customer");
                order.setQueriedAppId(queriedId);
                order.setProfileId(profileId);
                order.setTarget(null);

                FindConfigResult result = configService.findConfig(order);
                FacadeUtils.assertSuccess(result);
            }
        }
    }

    @Test
    public void testFindInheritedAppReleases() {
        String[] profileIds = new String[]{"offline", "dev"};
        for (String profileId : profileIds) {
            FindInheritedAppReleasesOrder order = new FindInheritedAppReleasesOrder();
            order.setMainAppId("customer");
            order.setQueriedAppId("customer");
            order.setProfileId(profileId);
            order.setTarget(null);

            FindInheritedAppReleasesResult result = configService.findInheritedAppReleases(order);
            FacadeUtils.assertSuccess(result);
        }
    }
}
