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
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.RevertBranchReleaseOrder;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.antframework.configcenter.facade.vo.ResultCode;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 回滚分支发布服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class RevertBranchReleaseService {
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
        checkTargetReleaseVersion(order, branch.getReleaseVersion());
        // 根据发布继承结构进行删除
        Long releaseVersion = branch.getReleaseVersion();
        Set<Long> reservedReleaseVersions = getReservedReleaseVersions(order);
        while (releaseVersion > ReleaseConstant.ORIGIN_VERSION && !reservedReleaseVersions.contains(releaseVersion)) {
            ReleaseInfo release = Releases.findRelease(order.getAppId(), order.getProfileId(), releaseVersion);
            if (release == null) {
                throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), releaseVersion));
            }
            try {
                Releases.deleteRelease(order.getAppId(), order.getProfileId(), releaseVersion);
            } catch (BizException e) {
                if (Objects.equals(e.getCode(), ResultCode.EXISTS_CHILDREN.getCode())) {
                    break;
                }
                throw e;
            }
            releaseVersion = release.getParentVersion();
        }
        // 更新分支
        branch.setReleaseVersion(order.getTargetReleaseVersion());
        branchDao.save(branch);
    }

    // 校验回滚到的目标发布版本
    private void checkTargetReleaseVersion(RevertBranchReleaseOrder order, Long branchReleaseVersion) {
        Long releaseVersion = branchReleaseVersion;
        while (releaseVersion > ReleaseConstant.ORIGIN_VERSION && !Objects.equals(releaseVersion, order.getTargetReleaseVersion())) {
            ReleaseInfo release = Releases.findRelease(order.getAppId(), order.getProfileId(), releaseVersion);
            if (release == null) {
                throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), releaseVersion));
            }
            releaseVersion = release.getParentVersion();
        }
        if (!Objects.equals(releaseVersion, order.getTargetReleaseVersion())) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在版本version=%d的发布", order.getAppId(), order.getProfileId(), order.getBranchId(), order.getTargetReleaseVersion()));
        }
    }

    // 获取需保存的发布版本
    private Set<Long> getReservedReleaseVersions(RevertBranchReleaseOrder order) {
        List<Branch> branches = branchDao.findLockByAppIdAndProfileId(order.getAppId(), order.getProfileId());
        Set<Long> releaseVersions = branches.stream()
                .filter(branch -> !Objects.equals(branch.getBranchId(), order.getBranchId()))
                .map(Branch::getReleaseVersion)
                .collect(Collectors.toSet());
        releaseVersions.add(order.getTargetReleaseVersion());
        return releaseVersions;
    }
}
