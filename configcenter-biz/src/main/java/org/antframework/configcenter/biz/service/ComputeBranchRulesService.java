/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-02 23:03 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.biz.util.BranchRules;
import org.antframework.configcenter.facade.info.BranchRuleInfo;
import org.antframework.configcenter.facade.order.ComputeBranchRulesOrder;
import org.antframework.configcenter.facade.result.ComputeBranchRulesResult;
import org.antframework.configcenter.facade.vo.BranchConstants;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 计算分支规则服务
 */
@Service
public class ComputeBranchRulesService {
    @ServiceExecute
    public void execute(ServiceContext<ComputeBranchRulesOrder, ComputeBranchRulesResult> context) {
        ComputeBranchRulesOrder order = context.getOrder();
        ComputeBranchRulesResult result = context.getResult();
        // 计算分支id
        String branchId = BranchConstants.DEFAULT_BRANCH_ID;
        if (order.getTarget() != null) {
            List<BranchRuleInfo> branchRules = BranchRules.findBranchRules(order.getAppId(), order.getProfileId());
            branchId = branchRules.stream()
                    .filter(branchRule -> Pattern.matches(branchRule.getRule(), order.getTarget()))
                    .map(BranchRuleInfo::getBranchId)
                    .findFirst()
                    .orElse(BranchConstants.DEFAULT_BRANCH_ID);
        }
        result.setBranchId(branchId);
    }
}
