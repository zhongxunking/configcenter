/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-28 22:58 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.BranchRules;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.PropertyValues;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.facade.order.DeleteBranchOrder;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 删除分支服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteBranchService {
    // 分支dao
    private final BranchDao branchDao;

    @ServiceBefore
    public void before(ServiceContext<DeleteBranchOrder, EmptyResult> context) {
        DeleteBranchOrder order = context.getOrder();
        // 删除配置value
        PropertyValues.deletePropertyValues(order.getAppId(), order.getProfileId(), order.getBranchId());
        // 删除分支规则
        BranchRules.deleteBranchRule(order.getAppId(), order.getProfileId(), order.getBranchId());
    }

    @ServiceExecute
    public void execute(ServiceContext<DeleteBranchOrder, EmptyResult> context) {
        DeleteBranchOrder order = context.getOrder();
        // 删除分支
        Branch branch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch != null) {
            Branches.revertBranch(order.getAppId(), order.getProfileId(), order.getBranchId(), ReleaseConstant.ORIGIN_VERSION);
            branchDao.delete(branch);
        }
    }
}
