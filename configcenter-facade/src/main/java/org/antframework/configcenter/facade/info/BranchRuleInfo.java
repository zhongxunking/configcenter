/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-01 19:58 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;

/**
 * 分支规则info
 */
@Getter
@Setter
public class BranchRuleInfo extends AbstractInfo {
    // 应用id
    private String appId;
    // 环境id
    private String profileId;
    // 分支id
    private String branchId;
    // 优先级
    private Long priority;
    // 规则
    private String rule;
}
