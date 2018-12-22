/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-11 22:55 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.AddReleaseResult;
import org.antframework.configcenter.facade.result.FindCurrentReleaseResult;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.result.QueryReleasesResult;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 发布服务单元测试
 */
@Ignore
public class ReleaseServiceTest extends AbstractTest {
    private static final String[] APP_IDS = new String[]{"common", "core-domain", "account", "customer"};
    private static final String[] PROFILE_IDS = new String[]{"offline", "dev"};

    @Autowired
    private ReleaseService releaseService;

    @Test
    public void testAddRelease() {
        for (String appId : APP_IDS) {
            for (String profileId : PROFILE_IDS) {
                AddReleaseOrder order = new AddReleaseOrder();
                order.setAppId(appId);
                order.setProfileId(profileId);
                order.setMemo(String.format("测试-%s-%s", appId, profileId));

                AddReleaseResult result = releaseService.addRelease(order);
                checkResult(result, Status.SUCCESS);
                Assert.assertNotNull(result.getRelease());
            }
        }
    }

    @Test
    public void testRevertRelease() {
        for (String appId : APP_IDS) {
            for (String profileId : PROFILE_IDS) {
                RevertReleaseOrder order = new RevertReleaseOrder();
                order.setAppId(appId);
                order.setProfileId(profileId);
                order.setVersion(ReleaseConstant.ORIGIN_VERSION);

                EmptyResult result = releaseService.revertRelease(order);
                checkResult(result, Status.SUCCESS);
            }
        }
    }

    @Test
    public void testFindCurrentRelease() {
        for (String appId : APP_IDS) {
            for (String profileId : PROFILE_IDS) {
                FindCurrentReleaseOrder order = new FindCurrentReleaseOrder();
                order.setAppId(appId);
                order.setProfileId(profileId);

                FindCurrentReleaseResult result = releaseService.findCurrentRelease(order);
                checkResult(result, Status.SUCCESS);
            }
        }
    }

    @Test
    public void testFindRelease() {
        for (String appId : APP_IDS) {
            for (String profileId : PROFILE_IDS) {
                FindReleaseOrder order = new FindReleaseOrder();
                order.setAppId(appId);
                order.setProfileId(profileId);
                order.setVersion(3L);

                FindReleaseResult result = releaseService.findRelease(order);
                checkResult(result, Status.SUCCESS);
            }
        }
    }

    @Test
    public void testQueryReleases() {
        for (String appId : APP_IDS) {
            for (String profileId : PROFILE_IDS) {
                QueryReleasesOrder order = new QueryReleasesOrder();
                order.setPageNo(1);
                order.setPageSize(10);
                order.setAppId(appId);
                order.setProfileId(profileId);

                QueryReleasesResult result = releaseService.queryReleases(order);
                checkResult(result, Status.SUCCESS);
            }
        }
    }
}
