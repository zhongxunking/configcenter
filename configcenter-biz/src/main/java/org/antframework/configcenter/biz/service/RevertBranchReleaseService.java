/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-26 22:55 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.RevertBranchReleaseOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 回滚分支发布服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class RevertBranchReleaseService {
    // 发布版本的key
    private static final String RELEASE_VERSION_KEY = "releaseVersion";

    // 分支dao
    private final BranchDao branchDao;

    @ServiceExecute
    public void execute(ServiceContext<RevertBranchReleaseOrder, EmptyResult> context) {
        RevertBranchReleaseOrder order = context.getOrder();
        // 校验
        Branch branch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
        ReleaseInfo release = Releases.findRelease(order.getAppId(), order.getProfileId(), order.getTargetReleaseVersion());
        if (release == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("回滚到的目标发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), order.getTargetReleaseVersion()));
        }
        // 保存发布版本
        context.setAttachmentAttr(RELEASE_VERSION_KEY, branch.getReleaseVersion());
        // 更新分支
        branch.setReleaseVersion(order.getTargetReleaseVersion());
        branchDao.save(branch);
    }

    @ServiceAfter
    public void after(ServiceContext<RevertBranchReleaseOrder, EmptyResult> context) {
        RevertBranchReleaseOrder order = context.getOrder();
        long releaseVersion = context.getAttachmentAttr(RELEASE_VERSION_KEY);
        // 删除分离的发布
        Branches.deleteDetachedReleases(order.getAppId(), order.getProfileId(), releaseVersion);
    }
}
