/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-01 18:35 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Refreshes;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.dao.BranchRuleDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.dal.entity.BranchRule;
import org.antframework.configcenter.facade.order.AddOrModifyBranchRuleOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;

/**
 * 新增或修改分支规则服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyBranchRuleService {
    // 分支dao
    private final BranchDao branchDao;
    // 分支规则dao
    private final BranchRuleDao branchRuleDao;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyBranchRuleOrder, EmptyResult> context) {
        AddOrModifyBranchRuleOrder order = context.getOrder();
        // 校验
        Branch branch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
        // 新增或修改
        BranchRule branchRule = branchRuleDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branchRule == null) {
            branchRule = new BranchRule();
        }
        BeanUtils.copyProperties(order, branchRule);
        branchRuleDao.save(branchRule);
    }

    @ServiceAfter
    public void after(ServiceContext<AddOrModifyBranchRuleOrder, EmptyResult> context) {
        AddOrModifyBranchRuleOrder order = context.getOrder();
        // 刷新客户端
        Refreshes.refreshClients(order.getAppId(), order.getProfileId());
    }
}
