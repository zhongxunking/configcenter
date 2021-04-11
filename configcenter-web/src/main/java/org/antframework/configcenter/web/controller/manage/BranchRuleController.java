/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-16 22:50 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.api.BranchRuleService;
import org.antframework.configcenter.facade.order.AddOrModifyBranchRuleOrder;
import org.antframework.configcenter.facade.order.DeleteBranchRuleOrder;
import org.antframework.configcenter.facade.order.FindBranchRulesOrder;
import org.antframework.configcenter.facade.result.FindBranchRulesResult;
import org.antframework.configcenter.web.common.ManagerApps;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分支规则controller
 */
@RestController
@RequestMapping("/manage/branchRule")
@AllArgsConstructor
public class BranchRuleController {
    // 分支规则服务
    private final BranchRuleService branchRuleService;

    /**
     * 新增或修改分支规则
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     * @param priority  优先级
     * @param rule      规则
     */
    @RequestMapping("/addOrModifyBranchRule")
    public EmptyResult addOrModifyBranchRule(String appId,
                                             String profileId,
                                             String branchId,
                                             String rule,
                                             Long priority) {
        ManagerApps.assertAdminOrHaveApp(appId);
        AddOrModifyBranchRuleOrder order = new AddOrModifyBranchRuleOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setRule(rule);
        order.setPriority(priority);

        return branchRuleService.addOrModifyBranchRule(order);
    }

    /**
     * 删除分支规则
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     */
    @RequestMapping("/deleteBranchRule")
    public EmptyResult deleteBranchRule(String appId, String profileId, String branchId) {
        ManagerApps.assertAdminOrHaveApp(appId);
        DeleteBranchRuleOrder order = new DeleteBranchRuleOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        return branchRuleService.deleteBranchRule(order);
    }

    /**
     * 查找分支规则
     *
     * @param appId     应用id
     * @param profileId 环境id
     */
    @RequestMapping("/findBranchRules")
    public FindBranchRulesResult findBranchRules(String appId, String profileId) {
        ManagerApps.assertAdminOrHaveApp(appId);
        FindBranchRulesOrder order = new FindBranchRulesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        return branchRuleService.findBranchRules(order);
    }
}
