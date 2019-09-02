/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-02 23:01 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;

/**
 * 计算分支规则result
 */
@Getter
@Setter
public class ComputeBranchRulesResult extends AbstractResult {
    // 计算出的分支id
    private String branchId;
}
