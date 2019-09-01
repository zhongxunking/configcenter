/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-01 19:58 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.BranchRuleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找分支规则result
 */
@Getter
public class FindBranchRulesResult extends AbstractResult {
    // 安优先级排序的分支规则
    private List<BranchRuleInfo> branchRules = new ArrayList<>();

    public void addBranchRule(BranchRuleInfo branchRule) {
        branchRules.add(branchRule);
    }
}
