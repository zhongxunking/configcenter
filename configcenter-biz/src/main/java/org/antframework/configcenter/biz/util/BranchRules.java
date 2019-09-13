/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-02 23:05 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.BranchRuleService;
import org.antframework.configcenter.facade.info.BranchRuleInfo;
import org.antframework.configcenter.facade.order.ComputeBranchRulesOrder;
import org.antframework.configcenter.facade.order.DeleteBranchRuleOrder;
import org.antframework.configcenter.facade.order.FindBranchRulesOrder;
import org.antframework.configcenter.facade.result.ComputeBranchRulesResult;
import org.antframework.configcenter.facade.result.FindBranchRulesResult;

import java.util.List;

/**
 * 分支规则操作类
 */
public final class BranchRules {
    // 分支规则服务
    private static final BranchRuleService BRANCH_RULE_SERVICE = Contexts.getApplicationContext().getBean(BranchRuleService.class);

    /**
     * 删除分支规则
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     */
    public static void deleteBranchRule(String appId, String profileId, String branchId) {
        DeleteBranchRuleOrder order = new DeleteBranchRuleOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        EmptyResult result = BRANCH_RULE_SERVICE.deleteBranchRule(order);
        FacadeUtils.assertSuccess(result);
    }

    /**
     * 查找分支规则
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @return 安优先级排序的分支规则
     */
    public static List<BranchRuleInfo> findBranchRules(String appId, String profileId) {
        FindBranchRulesOrder order = new FindBranchRulesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        FindBranchRulesResult result = BRANCH_RULE_SERVICE.findBranchRules(order);
        FacadeUtils.assertSuccess(result);
        return result.getBranchRules();
    }

    /**
     * 计算分支规则
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param target    被计算的目标
     * @return 计算出的分支id
     */
    public static String computeBranchRules(String appId, String profileId, String target) {
        ComputeBranchRulesOrder order = new ComputeBranchRulesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setTarget(target);

        ComputeBranchRulesResult result = BRANCH_RULE_SERVICE.computeBranchRules(order);
        FacadeUtils.assertSuccess(result);
        return result.getBranchId();
    }
}
