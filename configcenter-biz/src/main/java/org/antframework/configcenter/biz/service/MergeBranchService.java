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
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Properties;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.dao.MergenceDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.dal.entity.Mergence;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.MergeBranchOrder;
import org.antframework.configcenter.facade.result.MergeBranchResult;
import org.antframework.configcenter.facade.vo.Property;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
        // 校验
        Branch branch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
        Branch sourceBranch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getSourceBranchId());
        if (sourceBranch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("源分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getSourceBranchId()));
        }
        // 发布合并后的配置
        long startVersion = calcStartVersion(order.getAppId(), order.getProfileId(), branch.getReleaseVersion(), sourceBranch.getReleaseVersion());
        ReleaseInfo release = releaseDifference(order, startVersion, sourceBranch.getReleaseVersion());
        // 保存合并
        Mergence mergence = buildMergence(order, release.getVersion(), sourceBranch.getReleaseVersion());
        mergenceDao.save(mergence);
    }

    // 计算有效的最近的源发布
    private long calcStartVersion(String appId, String profileId, long targetVersion, long sourceVersion) {
        Set<Long> versions = new HashSet<>();
        versions.add(targetVersion);
        long startVersion = sourceVersion;
        // 计算被targetVersion覆盖的最近的sourceVersion的父版本
        while (!versions.contains(startVersion)) {
            long maxVersion = versions.stream().max(Long::compareTo).get();
            if (maxVersion > startVersion) {
                // 计算targetVersion覆盖的区域
                versions.remove(maxVersion);
                ReleaseInfo release = Releases.findRelease(appId, profileId, maxVersion);
                versions.add(release.getParentVersion());
                Mergence mergence = mergenceDao.findByAppIdAndProfileIdAndReleaseVersion(appId, profileId, maxVersion);
                if (mergence != null) {
                    versions.add(mergence.getSourceReleaseVersion());
                }
            } else {
                // 计算sourceVersion的父版本
                ReleaseInfo release = Releases.findRelease(appId, profileId, startVersion);
                startVersion = release.getParentVersion();
            }
        }
        return startVersion;
    }

    // 发布变更的配置
    private ReleaseInfo releaseDifference(MergeBranchOrder order, long startVersion, long endVersion) {
        Set<Property> startProperties = Releases.findRelease(order.getAppId(), order.getProfileId(), startVersion).getProperties();
        Set<Property> endProperties = Releases.findRelease(order.getAppId(), order.getProfileId(), endVersion).getProperties();
        // 计算变更的配置
        Properties.Difference difference = Properties.compare(endProperties, startProperties);
        Set<Property> addOrModifiedProperties = endProperties.stream()
                .filter(property -> difference.getAddedKeys().contains(property.getKey())
                        || difference.getModifiedValueKeys().contains(property.getKey())
                        || difference.getModifiedScopeKeys().contains(property.getKey()))
                .collect(Collectors.toSet());
        // 发布
        BranchInfo branch = Branches.releaseBranch(
                order.getAppId(),
                order.getProfileId(),
                order.getBranchId(),
                addOrModifiedProperties,
                difference.getRemovedKeys(),
                String.format("merge from branch[%s] with version[%d]", order.getSourceBranchId(), endVersion));
        return branch.getRelease();
    }

    // 构建合并
    private Mergence buildMergence(MergeBranchOrder order, long releaseVersion, Long sourceReleaseVersion) {
        Mergence mergence = new Mergence();
        BeanUtils.copyProperties(order, mergence);
        mergence.setReleaseVersion(releaseVersion);
        mergence.setSourceReleaseVersion(sourceReleaseVersion);

        return mergence;
    }
}
