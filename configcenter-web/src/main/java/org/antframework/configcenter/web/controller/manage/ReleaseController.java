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
import org.antframework.common.util.facade.*;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Properties;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.PropertiesDifference;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindInheritedAppReleasesOrder;
import org.antframework.configcenter.facade.order.FindReleaseOrder;
import org.antframework.configcenter.facade.order.QueryReleasesOrder;
import org.antframework.configcenter.facade.result.FindInheritedAppReleasesResult;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.result.QueryReleasesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.OperatePrivileges;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.web.CurrentManagerAssert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // 配置服务
    private final ConfigService configService;

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
        FindInheritedAppReleasesOrder order = new FindInheritedAppReleasesOrder();
        order.setMainAppId(appId);
        order.setQueriedAppId(appId);
        order.setProfileId(profileId);
        order.setTarget(null);

        FindInheritedAppReleasesResult result = configService.findInheritedAppReleases(order);
        if (result.isSuccess()) {
            // 设置分支的发布
            BranchInfo branch = Branches.findBranch(appId, profileId, branchId);
            if (branch == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", appId, profileId, branchId));
            }
            result.getInheritedAppReleases().get(0).getInheritedProfileReleases().set(0, branch.getRelease());

            if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
                // 敏感配置掩码
                result.getInheritedAppReleases().forEach(appRelease -> {
                    appRelease.getInheritedProfileReleases().forEach(this::maskRelease);
                });
            }
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
}
