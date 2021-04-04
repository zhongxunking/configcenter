/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-04 14:47 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.biz.util.BranchRules;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Profiles;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindInheritedAppReleasesOrder;
import org.antframework.configcenter.facade.result.FindInheritedAppReleasesResult;
import org.antframework.configcenter.facade.vo.AppRelease;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 查找继承的应用发布-service
 */
@Service
public class FindInheritedAppReleasesService {
    @ServiceExecute
    public void execute(ServiceContext<FindInheritedAppReleasesOrder, FindInheritedAppReleasesResult> context) {
        FindInheritedAppReleasesOrder order = context.getOrder();
        FindInheritedAppReleasesResult result = context.getResult();
        // 获取被查询配置的应用和主体应用继承的所有应用
        List<AppInfo> queriedApps = Apps.findInheritedApps(order.getQueriedAppId());
        Set<String> mainAppIds;
        if (Objects.equals(order.getMainAppId(), order.getQueriedAppId())) {
            mainAppIds = queriedApps.stream().map(AppInfo::getAppId).collect(Collectors.toSet());
        } else {
            mainAppIds = Apps.findInheritedApps(order.getMainAppId()).stream().map(AppInfo::getAppId).collect(Collectors.toSet());
        }
        // 获取继承的所有环境
        List<ProfileInfo> inheritedProfiles = Profiles.findInheritedProfiles(order.getProfileId());
        // 获取继承的应用发布
        for (AppInfo queriedApp : queriedApps) {
            // 获取继承的环境发布
            List<ReleaseInfo> inheritedProfileReleases = new ArrayList<>(inheritedProfiles.size());
            Scope minScope = computeMinScope(queriedApp.getAppId(), order.getMainAppId(), mainAppIds);
            for (ProfileInfo profile : inheritedProfiles) {
                ReleaseInfo release = getRelease(queriedApp.getAppId(), profile.getProfileId(), order.getTarget(), minScope);
                inheritedProfileReleases.add(release);
            }
            // 添加继承的应用发布
            AppRelease appRelease = new AppRelease(queriedApp, inheritedProfileReleases);
            result.addInheritedAppRelease(appRelease);
        }
    }

    // 计算最小作用域
    private Scope computeMinScope(String queriedAppId, String mainAppId, Set<String> mainAppIds) {
        if (Objects.equals(queriedAppId, mainAppId)) {
            return Scope.PRIVATE;
        } else if (mainAppIds.contains(queriedAppId)) {
            return Scope.PROTECTED;
        } else {
            return Scope.PUBLIC;
        }
    }

    // 获取发布
    private ReleaseInfo getRelease(String appId, String profileId, String target, Scope minScope) {
        // 获取分支
        String branchId = BranchRules.computeBranchRules(appId, profileId, target);
        BranchInfo branch = Branches.findBranch(appId, profileId, branchId);
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", appId, profileId, branchId));
        }
        // 移除作用域不合要求的配置
        ReleaseInfo release = branch.getRelease();
        release.getProperties().removeIf(property -> property.getScope().compareTo(minScope) < 0);

        return release;
    }
}
