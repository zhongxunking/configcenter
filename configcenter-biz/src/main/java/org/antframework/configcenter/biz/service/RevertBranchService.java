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
import org.antframework.configcenter.biz.util.Refreshes;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.dao.MergenceDao;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.dal.entity.Mergence;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.order.RevertBranchOrder;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
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
public class RevertBranchService {
    // 分支dao
    private final BranchDao branchDao;
    // 发布dao
    private final ReleaseDao releaseDao;
    // 合并dao
    private final MergenceDao mergenceDao;

    @ServiceExecute
    public void execute(ServiceContext<RevertBranchOrder, EmptyResult> context) {
        RevertBranchOrder order = context.getOrder();
        // 校验
        Branch branch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
        if (order.getTargetReleaseVersion() > ReleaseConstant.ORIGIN_VERSION) {
            Release targetRelease = releaseDao.findLockByAppIdAndProfileIdAndVersion(order.getAppId(), order.getProfileId(), order.getTargetReleaseVersion());
            if (targetRelease == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("回滚到的目标发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), order.getTargetReleaseVersion()));
            }
        }
        // 删除未被关联的发布
        Set<Long> touchedReleaseVersions = getTouchedReleaseVersions(branch, order.getTargetReleaseVersion());
        deleteDetachedReleases(branch, touchedReleaseVersions);
        // 更新分支
        branch.setReleaseVersion(order.getTargetReleaseVersion());
        branchDao.save(branch);
    }

    // 获取被关联的发布的版本
    private Set<Long> getTouchedReleaseVersions(Branch branch, long targetReleaseVersion) {
        List<Branch> branches = branchDao.findLockByAppIdAndProfileId(branch.getAppId(), branch.getProfileId());
        Set<Long> versions = branches.stream()
                .filter(b -> !Objects.equals(b.getBranchId(), branch.getBranchId()))
                .map(Branch::getReleaseVersion)
                .collect(Collectors.toSet());
        versions.add(targetReleaseVersion);
        return versions;
    }

    // 删除未被关联的发布
    private void deleteDetachedReleases(Branch branch, Set<Long> touchedReleaseVersions) {
        long version = branch.getReleaseVersion();
        while (version > ReleaseConstant.ORIGIN_VERSION && !touchedReleaseVersions.contains(version)) {
            // 判断是否删除发布
            Release release = releaseDao.findLockByAppIdAndProfileIdAndVersion(branch.getAppId(), branch.getProfileId(), version);
            if (release == null) {
                throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("发布[appId=%s,profileId=%s,version=%d]不存在", branch.getAppId(), branch.getProfileId(), version));
            }
            boolean existsChildren = releaseDao.existsByAppIdAndProfileIdAndParentVersion(branch.getAppId(), branch.getProfileId(), version);
            if (existsChildren) {
                break;
            }
            // 删除相关的合并
            deleteMergences(branch.getAppId(), branch.getProfileId(), version);
            // 删除发布
            releaseDao.delete(release);
            // 切换到父版本
            version = release.getParentVersion();
        }
    }

    // 删除合并
    private void deleteMergences(String appId, String profileId, long version) {
        Mergence mergence = mergenceDao.findLockByAppIdAndProfileIdAndReleaseVersion(appId, profileId, version);
        if (mergence != null) {
            mergenceDao.delete(mergence);
        }
        List<Mergence> mergences = mergenceDao.findLockByAppIdAndProfileIdAndSourceReleaseVersion(appId, profileId, version);
        mergences.forEach(mergenceDao::delete);
    }

    @ServiceAfter
    public void after(ServiceContext<RevertBranchOrder, EmptyResult> context) {
        RevertBranchOrder order = context.getOrder();
        // 刷新客户端
        Refreshes.refreshClients(order.getAppId(), order.getProfileId());
    }
}
