/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-23 17:52 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.biz.util.*;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertiesDifference;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindReleaseOrder;
import org.antframework.configcenter.facade.order.QueryReleasesOrder;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.result.QueryReleasesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.OperatePrivileges;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.web.CurrentManagerAssert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 发布controller
 */
@RestController
@RequestMapping("/manage/release")
@AllArgsConstructor
public class ReleaseController {
    // 发布服务
    private final ReleaseService releaseService;

    /**
     * 查找发布
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param version   版本
     */
    @RequestMapping("/findRelease")
    public FindReleaseResult findRelease(String appId, String profileId, Long version) {
        ManagerApps.assertAdminOrHaveApp(appId);
        FindReleaseOrder order = new FindReleaseOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setVersion(version);

        FindReleaseResult result = releaseService.findRelease(order);
        if (result.isSuccess() && CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            if (result.getRelease() != null) {
                maskRelease(result.getRelease());
            }
        }
        return result;
    }

    /**
     * 查找继承的应用发布
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     */
    @RequestMapping("/findInheritedAppReleases")
    public FindInheritedAppReleasesResult findInheritedAppReleases(String appId, String profileId, String branchId) {
        ManagerApps.assertAdminOrHaveApp(appId);

        FindInheritedAppReleasesResult result = FacadeUtils.buildSuccess(FindInheritedAppReleasesResult.class);
        for (AppInfo app : Apps.findInheritedApps(appId)) {
            // 获取应用在各环境的发布
            Scope minScope = Objects.equals(app.getAppId(), appId) ? Scope.PRIVATE : Scope.PROTECTED;
            List<ReleaseInfo> inheritedProfileReleases = Configs.findAppSelfConfig(app.getAppId(), profileId, minScope, null);// 掩码
            if (Objects.equals(app.getAppId(), appId)) {
                inheritedProfileReleases.set(0, Branches.findBranch(appId, profileId, branchId).getRelease());
            }
            if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
                inheritedProfileReleases.forEach(this::maskRelease);
            }
            FindInheritedAppReleasesResult.AppRelease appRelease = new FindInheritedAppReleasesResult.AppRelease(app, inheritedProfileReleases);

            result.addInheritedAppRelease(appRelease);
        }
        return result;
    }

    /**
     * 比较两个发布的配置差异
     *
     * @param appId        应用id
     * @param profileId    环境id
     * @param leftVersion  待比较的发布版本
     * @param rightVersion 待比较的发布版本
     */
    @RequestMapping("/compareReleases")
    public CompareReleasesResult compareReleases(String appId, String profileId, Long leftVersion, Long rightVersion) {
        ManagerApps.assertAdminOrHaveApp(appId);

        Set<Property> left = Releases.findRelease(appId, profileId, leftVersion).getProperties();
        Set<Property> right = Releases.findRelease(appId, profileId, rightVersion).getProperties();
        PropertiesDifference difference = Properties.compare(left, right);

        CompareReleasesResult result = FacadeUtils.buildSuccess(CompareReleasesResult.class);
        result.setDifference(difference);
        return result;
    }

    /**
     * 查询发布
     *
     * @param pageNo        页码
     * @param pageSize      每页大小
     * @param appId         应用id
     * @param profileId     环境id
     * @param version       版本
     * @param memo          备注
     * @param parentVersion 父版本
     */
    @RequestMapping("/queryReleases")
    public QueryReleasesResult queryReleases(int pageNo,
                                             int pageSize,
                                             String appId,
                                             String profileId,
                                             Long version,
                                             String memo,
                                             Long parentVersion) {
        if (appId == null) {
            CurrentManagerAssert.admin();
        } else {
            ManagerApps.assertAdminOrHaveApp(appId);
        }
        QueryReleasesOrder order = new QueryReleasesOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setVersion(version);
        order.setMemo(memo);
        order.setParentVersion(parentVersion);

        QueryReleasesResult result = releaseService.queryReleases(order);
        if (result.isSuccess() && CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            result.getInfos().forEach(this::maskRelease);
        }
        return result;
    }

    // 掩码敏感配置
    private void maskRelease(ReleaseInfo release) {
        Set<Property> maskedProperties = OperatePrivileges.maskProperties(release.getAppId(), release.getProperties());
        release.setProperties(maskedProperties);
    }

    /**
     * 比较两个发布的配置差异--result
     */
    @Getter
    @Setter
    public static class CompareReleasesResult extends AbstractResult {
        // 差异
        private PropertiesDifference difference;
    }

    /**
     * 查找继承的应用发布--result
     */
    @Getter
    public static class FindInheritedAppReleasesResult extends AbstractResult {
        // 由近及远继承的应用发布
        private final List<AppRelease> inheritedAppReleases = new ArrayList<>();

        public void addInheritedAppRelease(AppRelease appRelease) {
            inheritedAppReleases.add(appRelease);
        }

        /**
         * 应用发布
         */
        @AllArgsConstructor
        @Getter
        public static class AppRelease implements Serializable {
            // 应用
            private final AppInfo app;
            // 由近及远继承的环境中的发布
            private final List<ReleaseInfo> inheritedProfileReleases;

            @Override
            public String toString() {
                return ToString.toString(this);
            }
        }
    }
}
