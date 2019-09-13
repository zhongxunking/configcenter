/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-01 19:51 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.Refreshes;
import org.antframework.configcenter.dal.dao.BranchRuleDao;
import org.antframework.configcenter.dal.entity.BranchRule;
import org.antframework.configcenter.facade.order.DeleteBranchRuleOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 删除分支规则服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteBranchRuleService {
    // 分支规则dao
    private final BranchRuleDao branchRuleDao;

    @ServiceExecute
    public void execute(ServiceContext<DeleteBranchRuleOrder, EmptyResult> context) {
        DeleteBranchRuleOrder order = context.getOrder();
        // 删除分支规则
        BranchRule branchRule = branchRuleDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branchRule != null) {
            branchRuleDao.delete(branchRule);
        }
    }

    @ServiceAfter
    public void after(ServiceContext<DeleteBranchRuleOrder, EmptyResult> context) {
        DeleteBranchRuleOrder order = context.getOrder();
        // 刷新客户端
        Refreshes.refreshClients(order.getAppId(), order.getProfileId());
    }
}
