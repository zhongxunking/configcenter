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
import org.antframework.configcenter.facade.info.PropertiesDifference;
import org.antframework.configcenter.facade.info.PropertyChange;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.ComputeBranchMergenceOrder;
import org.antframework.configcenter.facade.result.ComputeBranchMergenceResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.HashSet;
import java.util.Set;

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
        ReleaseInfo recentRelease = computeRecentRelease(
                order.getAppId(),
                order.getProfileId(),
                branch.getRelease().getVersion(),
                sourceBranch.getRelease().getVersion());
        // 计算配置变动
        PropertyChange propertyChange = computeChange(recentRelease, sourceBranch.getRelease());
        result.setPropertyChange(propertyChange);
    }

    // 计算最近的发布
    private ReleaseInfo computeRecentRelease(String appId, String profileId, long targetVersion, long sourceVersion) {
        Set<Long> versions = new HashSet<>();
        versions.add(targetVersion);
        long version = sourceVersion;
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

    // 计算需合并的配置变动
    private PropertyChange computeChange(ReleaseInfo startRelease, ReleaseInfo endRelease) {
        PropertyChange propertyChange = new PropertyChange();
        PropertiesDifference propertiesDifference = Properties.compare(endRelease.getProperties(), startRelease.getProperties());
        endRelease.getProperties().stream()
                .filter(property -> propertiesDifference.getAddedKeys().contains(property.getKey())
                        || propertiesDifference.getModifiedValueKeys().contains(property.getKey())
                        || propertiesDifference.getModifiedScopeKeys().contains(property.getKey()))
                .forEach(propertyChange::addAddedOrModifiedProperty);
        propertiesDifference.getRemovedKeys().forEach(propertyChange::addDeletedKey);

        return propertyChange;
    }
}
