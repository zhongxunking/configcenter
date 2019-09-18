/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-18 23:00 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.BranchService;
import org.antframework.configcenter.facade.order.AddBranchOrder;
import org.antframework.configcenter.facade.vo.BranchConstants;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分支服务单元测试
 */
public class BranchServiceTest extends AbstractTest {
    @Autowired
    private BranchService branchService;

    @Test
    public void testAddBranch() {
        AddBranchOrder order = new AddBranchOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setBranchId(BranchConstants.DEFAULT_BRANCH_ID);
        order.setReleaseVersion(0L);

        EmptyResult result = branchService.addBranch(order);
        FacadeUtils.assertSuccess(result);
    }
}
