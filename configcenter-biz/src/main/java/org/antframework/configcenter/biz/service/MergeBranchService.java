/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-29 22:48 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.facade.order.MergeBranchOrder;
import org.antframework.configcenter.facade.result.MergeBranchResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 合并分支服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class MergeBranchService {
    // 分支dao
    private final BranchDao branchDao;

    @ServiceExecute
    public void execute(ServiceContext<MergeBranchOrder, MergeBranchResult> context) {
        MergeBranchOrder order = context.getOrder();

        Branch branch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
        Branch sourceBranch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getSourceBranchId());
        if (sourceBranch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("源分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getSourceBranchId()));
        }


    }
}
