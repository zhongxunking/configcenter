/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-23 21:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindBranchReleaseOrder;
import org.antframework.configcenter.facade.result.FindBranchReleaseResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找分支发布服务
 */
@Service
public class FindBranchReleaseService {
    @ServiceExecute
    public void execute(ServiceContext<FindBranchReleaseOrder, FindBranchReleaseResult> context) {
        FindBranchReleaseOrder order = context.getOrder();
        FindBranchReleaseResult result = context.getResult();

        BranchInfo branch = Branches.findBranch(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
        ReleaseInfo release = Releases.findRelease(branch.getAppId(), branch.getProfileId(), branch.getReleaseVersion());
        if (release == null) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]对应的发布版本[%d]不存在", order.getAppId(), order.getProfileId(), order.getBranchId(), branch.getReleaseVersion()));
        }
        result.setRelease(release);
    }
}
