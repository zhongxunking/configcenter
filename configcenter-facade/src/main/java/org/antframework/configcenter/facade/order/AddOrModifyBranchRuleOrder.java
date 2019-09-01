/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-01 14:29 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增或修改分支规则order
 */
@Getter
@Setter
public class AddOrModifyBranchRuleOrder {
    // 应用id
    @NotBlank
    private String appId;
    // 环境id
    @NotBlank
    private String profileId;
    // 分支id
    @NotBlank
    private String branchId;
    // 规则
    @NotBlank
    private String rule;
    // 优先级
    @NotNull
    private Long priority;
}
