/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-05 23:36 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Properties;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.dal.dao.MergenceDao;
import org.antframework.configcenter.dal.entity.Mergence;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.ComputeBranchMergenceOrder;
import org.antframework.configcenter.facade.result.ComputeBranchMergenceResult;
import org.antframework.configcenter.facade.vo.Property;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 计算分支合并服务
 */
@Service
@AllArgsConstructor
public class ComputeBranchMergenceService {
    // 合并dao
    private final MergenceDao mergenceDao;

    @ServiceExecute
    public void execute(ServiceContext<ComputeBranchMergenceOrder, ComputeBranchMergenceResult> context) {
        ComputeBranchMergenceOrder order = context.getOrder();
        ComputeBranchMergenceResult result = context.getResult();
        // 校验
        BranchInfo branch = Branches.findBranch(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
        BranchInfo sourceBranch = Branches.findBranch(order.getAppId(), order.getProfileId(), order.getSourceBranchId());
        if (sourceBranch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getSourceBranchId()));
        }
        // 计算最近的发布
        ReleaseInfo rencentRelease = computeRecentRelease(
                order.getAppId(),
                order.getProfileId(),
                branch.getRelease().getVersion(),
                sourceBranch.getRelease().getVersion());
        // 计算变更的配置
        computeDifference(rencentRelease, sourceBranch.getRelease(), result);
    }

    // 计算最近的发布
    private ReleaseInfo computeRecentRelease(String appId, String profileId, long sourceVersion, long targetVersion) {
        long version = sourceVersion;
        Set<Long> versions = new HashSet<>();
        versions.add(targetVersion);
        // 从targetVersion继承体系中，找到被targetVersion覆盖的最近的发布版本
        while (!versions.contains(version)) {
            long maxVersion = versions.stream().max(Long::compareTo).get();
            if (maxVersion > version) {
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
                ReleaseInfo release = Releases.findRelease(appId, profileId, version);
                version = release.getParentVersion();
            }
        }
        return Releases.findRelease(appId, profileId, version);
    }

    // 计算变更的配置
    private void computeDifference(ReleaseInfo startRelease, ReleaseInfo endRelease, ComputeBranchMergenceResult result) {
        // 计算变更的配置
        Properties.Difference difference = Properties.compare(endRelease.getProperties(), startRelease.getProperties());
        Set<Property> addOrModifiedProperties = endRelease.getProperties().stream()
                .filter(property -> difference.getAddedKeys().contains(property.getKey())
                        || difference.getModifiedValueKeys().contains(property.getKey())
                        || difference.getModifiedScopeKeys().contains(property.getKey()))
                .collect(Collectors.toSet());
        // 设置结果
        result.setAddOrModifiedProperties(addOrModifiedProperties);
        result.setRemovedPropertyKeys(difference.getRemovedKeys());
    }
}
