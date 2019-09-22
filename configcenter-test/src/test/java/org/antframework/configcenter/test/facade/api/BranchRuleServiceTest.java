/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-22 21:29 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.BranchRuleService;
import org.antframework.configcenter.facade.order.AddOrModifyBranchRuleOrder;
import org.antframework.configcenter.facade.order.ComputeBranchRulesOrder;
import org.antframework.configcenter.facade.order.DeleteBranchRuleOrder;
import org.antframework.configcenter.facade.order.FindBranchRulesOrder;
import org.antframework.configcenter.facade.result.ComputeBranchRulesResult;
import org.antframework.configcenter.facade.result.FindBranchRulesResult;
import org.antframework.configcenter.facade.vo.BranchConstants;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分支规则服务单元测试
 */
@Ignore
public class BranchRuleServiceTest extends AbstractTest {
    @Autowired
    private BranchRuleService branchRuleService;

    @Test
    public void testAddOrModifyBranchRule() {
        AddOrModifyBranchRuleOrder order = new AddOrModifyBranchRuleOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setBranchId(BranchConstants.DEFAULT_BRANCH_ID);
        order.setPriority(0L);
        order.setRule("abc.*");

        EmptyResult result = branchRuleService.addOrModifyBranchRule(order);
        FacadeUtils.assertSuccess(result);
    }

    @Test
    public void testDeleteBranchRule() {
        DeleteBranchRuleOrder order = new DeleteBranchRuleOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setBranchId(BranchConstants.DEFAULT_BRANCH_ID);

        EmptyResult result = branchRuleService.deleteBranchRule(order);
        FacadeUtils.assertSuccess(result);
    }

    @Test
    public void testFindBranchRules() {
        FindBranchRulesOrder order = new FindBranchRulesOrder();
        order.setAppId("customer");
        order.setProfileId("dev");

        FindBranchRulesResult result = branchRuleService.findBranchRules(order);
        FacadeUtils.assertSuccess(result);
    }

    @Test
    public void testComputeBranchRules() {
        ComputeBranchRulesOrder order = new ComputeBranchRulesOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setTarget("abc");

        ComputeBranchRulesResult result = branchRuleService.computeBranchRules(order);
        FacadeUtils.assertSuccess(result);
    }
}
