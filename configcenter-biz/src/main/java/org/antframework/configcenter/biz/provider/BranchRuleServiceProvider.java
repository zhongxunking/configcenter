/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-01 18:33 创建
 */
package org.antframework.configcenter.biz.provider;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.api.BranchRuleService;
import org.antframework.configcenter.facade.order.AddOrModifyBranchRuleOrder;
import org.bekit.service.ServiceEngine;
import org.springframework.stereotype.Service;

/**
 * 分支规则服务提供者
 */
@Service
@AllArgsConstructor
public class BranchRuleServiceProvider implements BranchRuleService {
    // 服务引擎
    private final ServiceEngine serviceEngine;

    @Override
    public EmptyResult addOrModifyBranchRule(AddOrModifyBranchRuleOrder order) {
        return serviceEngine.execute("addOrModifyBranchRuleService", order);
    }
}
