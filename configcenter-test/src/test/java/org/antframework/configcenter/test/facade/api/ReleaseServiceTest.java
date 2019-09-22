/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-11 22:55 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.order.FindReleaseOrder;
import org.antframework.configcenter.facade.order.QueryReleasesOrder;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.result.QueryReleasesResult;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 发布服务单元测试
 */
@Ignore
public class ReleaseServiceTest extends AbstractTest {
    @Autowired
    private ReleaseService releaseService;

    @Test
    public void testFindRelease() {
        FindReleaseOrder order = new FindReleaseOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setVersion(ReleaseConstant.ORIGIN_VERSION);

        FindReleaseResult result = releaseService.findRelease(order);
        FacadeUtils.assertSuccess(result);
    }

    @Test
    public void testQueryReleases() {
        QueryReleasesOrder order = new QueryReleasesOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        order.setAppId(null);
        order.setProfileId(null);
        order.setVersion(null);
        order.setMemo(null);
        order.setParentVersion(null);

        QueryReleasesResult result = releaseService.queryReleases(order);
        FacadeUtils.assertSuccess(result);
    }
}
