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
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Refreshes;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.dao.MergenceDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.dal.entity.Mergence;
import org.antframework.configcenter.facade.info.PropertyChange;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.MergeBranchOrder;
import org.antframework.configcenter.facade.result.MergeBranchResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;

/**
 * 合并分支服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class MergeBranchService {
    // 分支dao
    private final BranchDao branchDao;
    // 合并dao
    private final MergenceDao mergenceDao;

    @ServiceExecute
    public void execute(ServiceContext<MergeBranchOrder, MergeBranchResult> context) {
        MergeBranchOrder order = context.getOrder();
        MergeBranchResult result = context.getResult();
        // 校验
        Branch branch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
        Branch sourceBranch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getSourceBranchId());
        if (sourceBranch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getSourceBranchId()));
        }
        long sourceReleaseVersion = sourceBranch.getReleaseVersion();
        // 计算分支合并的配置变动
        PropertyChange propertyChange = Branches.computeBranchMergence(
                order.getAppId(),
                order.getProfileId(),
                order.getBranchId(),
                order.getSourceBranchId());
        // 发布变更
        ReleaseInfo release = Branches.releaseBranch(
                order.getAppId(),
                order.getProfileId(),
                order.getBranchId(),
                propertyChange,
                String.format("merged from branch[%s] with release version[%d]", order.getSourceBranchId(), sourceReleaseVersion))
                .getRelease();
        // 保存合并
        Mergence mergence = buildMergence(order, release.getVersion(), sourceReleaseVersion);
        mergenceDao.save(mergence);

        result.setPropertyChange(propertyChange);
    }

    // 构建合并
    private Mergence buildMergence(MergeBranchOrder order, long releaseVersion, Long sourceReleaseVersion) {
        Mergence mergence = new Mergence();
        BeanUtils.copyProperties(order, mergence);
        mergence.setReleaseVersion(releaseVersion);
        mergence.setSourceReleaseVersion(sourceReleaseVersion);

        return mergence;
    }

    @ServiceAfter
    public void after(ServiceContext<MergeBranchOrder, EmptyResult> context) {
        MergeBranchOrder order = context.getOrder();
        // 刷新客户端
        Refreshes.refreshClients(order.getAppId(), order.getProfileId());
    }
}
