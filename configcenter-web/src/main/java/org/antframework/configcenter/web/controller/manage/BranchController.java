/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-14 19:18 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Properties;
import org.antframework.configcenter.biz.util.PropertyValues;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.facade.api.BranchService;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.PropertyChange;
import org.antframework.configcenter.facade.info.PropertyDifference;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.FindBranchResult;
import org.antframework.configcenter.facade.result.FindBranchesResult;
import org.antframework.configcenter.facade.result.MergeBranchResult;
import org.antframework.configcenter.facade.vo.BranchConstants;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.web.common.AppPropertyTypes;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.web.CurrentManagerAssert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分支controller
 */
@RestController
@RequestMapping("/manage/branch")
@AllArgsConstructor
public class BranchController {
    // 分支服务
    private final BranchService branchService;

    /**
     * 添加分支
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param releaseVersion 发布版本
     */
    @RequestMapping("/addBranch")
    public EmptyResult addBranch(String appId,
                                 String profileId,
                                 String branchId,
                                 Long releaseVersion) {
        ManagerApps.assertAdminOrHaveApp(appId);
        AddBranchOrder order = new AddBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setReleaseVersion(releaseVersion);

        EmptyResult result = branchService.addBranch(order);
        if (result.isSuccess()) {
            PropertyValues.revertPropertyValues(appId, profileId, branchId, releaseVersion);
        }
        return result;
    }

    /**
     * 发布分支
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param propertyChange 配置变动
     * @param memo           备注
     */
    @RequestMapping("/releaseBranch")
    public ReleaseBranchResult releaseBranch(String appId,
                                             String profileId,
                                             String branchId,
                                             @RequestParam PropertyChange propertyChange,
                                             String memo) {
        // 验权
        ManagerApps.assertAdminOrHaveApp(appId);
        if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            Set<String> keys = propertyChange.getAddedOrModifiedProperties().stream().map(Property::getKey).collect(Collectors.toSet());
            keys.addAll(propertyChange.getDeletedKeys());
            AppPropertyTypes.assertAdminOrOnlyReadWrite(appId, keys);
        }

        ReleaseBranchOrder order = new ReleaseBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setPropertyChange(propertyChange);
        order.setMemo(memo);

        ReleaseBranchResult result = branchService.releaseBranch(order);
        if (result.isSuccess()) {
            // 同步到配置value
            PropertyValues.changePropertyValues(
                    appId,
                    profileId,
                    branchId,
                    propertyChange);
            // 对敏感配置掩码
            if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
                maskRelease(result.getBranch().getRelease());
            }
        }
        return result;
    }

    /**
     * 回滚分支
     *
     * @param appId                应用id
     * @param profileId            环境id
     * @param branchId             分支id
     * @param targetReleaseVersion 回滚到的目标发布版本
     */
    @RequestMapping("/revertBranch")
    public EmptyResult revertBranch(String appId,
                                    String profileId,
                                    String branchId,
                                    Long targetReleaseVersion) {
        // 验权
        ManagerApps.assertAdminOrHaveApp(appId);
        if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN && Objects.equals(branchId, BranchConstants.DEFAULT_BRANCH_ID)) {
            Set<String> keys = new HashSet<>();
            Set<Property> startProperties = Branches.findBranch(appId, profileId, branchId).getRelease().getProperties();
            Set<Property> endProperties = Releases.findRelease(appId, profileId, targetReleaseVersion).getProperties();
            PropertyDifference difference = Properties.compare(endProperties, startProperties);
            keys.addAll(difference.getAddedKeys());
            keys.addAll(difference.getModifiedValueKeys());
            keys.addAll(difference.getModifiedScopeKeys());
            keys.addAll(difference.getDeletedKeys());
            AppPropertyTypes.assertAdminOrOnlyReadWrite(appId, keys);
        }

        RevertBranchOrder order = new RevertBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setTargetReleaseVersion(targetReleaseVersion);

        EmptyResult result = branchService.revertBranch(order);
        if (result.isSuccess()) {
            PropertyValues.revertPropertyValues(
                    appId,
                    profileId,
                    branchId,
                    targetReleaseVersion);
        }
        return result;
    }

    /**
     * 合并分支
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param sourceBranchId 源分支id
     */
    @RequestMapping("/mergeBranch")
    public MergeBranchResult mergeBranch(String appId,
                                         String profileId,
                                         String branchId,
                                         String sourceBranchId) {
        // 验权
        ManagerApps.assertAdminOrHaveApp(appId);
        if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN && Objects.equals(branchId, BranchConstants.DEFAULT_BRANCH_ID)) {
            Set<String> keys = new HashSet<>();
            ComputeBranchMergenceResult computeBranchMergenceResult = computeBranchMergence(
                    appId,
                    profileId,
                    branchId,
                    sourceBranchId);
            FacadeUtils.assertSuccess(computeBranchMergenceResult);
            keys.addAll(computeBranchMergenceResult.getDifference().getAddedKeys());
            keys.addAll(computeBranchMergenceResult.getDifference().getModifiedValueKeys());
            keys.addAll(computeBranchMergenceResult.getDifference().getModifiedScopeKeys());
            keys.addAll(computeBranchMergenceResult.getDifference().getDeletedKeys());
            AppPropertyTypes.assertAdminOrOnlyReadWrite(appId, keys);
        }

        MergeBranchOrder order = new MergeBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setSourceBranchId(sourceBranchId);

        MergeBranchResult result = branchService.mergeBranch(order);
        if (result.isSuccess()) {
            // 同步到配置value
            PropertyValues.changePropertyValues(
                    appId,
                    profileId,
                    branchId,
                    result.getPropertyChange());
            // 对敏感配置掩码
            if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
                Set<Property> properties = result.getPropertyChange().getAddedOrModifiedProperties();
                Set<Property> maskedProperties = AppPropertyTypes.maskProperties(appId, properties);
                properties.clear();
                properties.addAll(maskedProperties);
            }
        }
        return result;
    }

    /**
     * 计算分支合并
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param sourceBranchId 源分支id
     */
    @RequestMapping("/computeBranchMergence")
    public ComputeBranchMergenceResult computeBranchMergence(String appId,
                                                             String profileId,
                                                             String branchId,
                                                             String sourceBranchId) {
        ManagerApps.assertAdminOrHaveApp(appId);
        // 计算分支合并的配置变动
        PropertyChange propertyChange = Branches.computeBranchMergence(appId, profileId, branchId, sourceBranchId);
        Set<Property> oldProperties = Branches.findBranch(appId, profileId, branchId).getRelease().getProperties();
        Set<Property> changedProperties = new HashSet<>();
        // 计算真正新增或修改的配置
        PropertyDifference difference = Properties.compare(propertyChange.getAddedOrModifiedProperties(), oldProperties);
        difference.getDeletedKeys().clear();
        propertyChange.getAddedOrModifiedProperties().stream()
                .filter(property -> difference.getAddedKeys().contains(property.getKey())
                        || difference.getModifiedValueKeys().contains(property.getKey())
                        || difference.getModifiedScopeKeys().contains(property.getKey()))
                .forEach(changedProperties::add);
        // 计算真正删除的配置
        oldProperties.stream()
                .filter(property -> propertyChange.getDeletedKeys().contains(property.getKey()))
                .forEach(property -> {
                    difference.addDeletedKeys(property.getKey());
                    changedProperties.add(property);
                });

        ComputeBranchMergenceResult result = FacadeUtils.buildSuccess(ComputeBranchMergenceResult.class);
        if (CurrentManagerAssert.current().getType() == ManagerType.ADMIN) {
            result.setChangedProperties(changedProperties);
        } else {
            // 对敏感配置掩码
            result.setChangedProperties(AppPropertyTypes.maskProperties(appId, changedProperties));
        }
        result.setDifference(difference);
        return result;
    }

    /**
     * 删除分支
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     */
    @RequestMapping("/deleteBranch")
    public EmptyResult deleteBranch(String appId, String profileId, String branchId) {
        // 验权
        ManagerApps.assertAdminOrHaveApp(appId);
        if (Objects.equals(branchId, BranchConstants.DEFAULT_BRANCH_ID)) {
            CurrentManagerAssert.admin();
        }

        DeleteBranchOrder order = new DeleteBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        return branchService.deleteBranch(order);
    }

    /**
     * 查找分支
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     */
    @RequestMapping("/findBranch")
    public FindBranchResult findBranch(String appId, String profileId, String branchId) {
        ManagerApps.assertAdminOrHaveApp(appId);
        FindBranchOrder order = new FindBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        FindBranchResult result = branchService.findBranch(order);
        if (result.isSuccess() && CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            maskRelease(result.getBranch().getRelease());
        }
        return result;
    }

    /**
     * 查找应用在环境下的所有分支
     *
     * @param appId     应用id
     * @param profileId 环境id
     */
    @RequestMapping("/findBranches")
    public FindBranchesResult findBranches(String appId, String profileId) {
        ManagerApps.assertAdminOrHaveApp(appId);
        FindBranchesOrder order = new FindBranchesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        FindBranchesResult result = branchService.findBranches(order);
        if (result.isSuccess() && CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            result.getBranches().stream().map(BranchInfo::getRelease).forEach(this::maskRelease);
        }
        return result;
    }

    // 掩码敏感配置
    private void maskRelease(ReleaseInfo release) {
        Set<Property> maskedProperties = AppPropertyTypes.maskProperties(release.getAppId(), release.getProperties());
        release.setProperties(maskedProperties);
    }

    /**
     * 计算分支合并result
     */
    @Getter
    @Setter
    public static class ComputeBranchMergenceResult extends AbstractResult {
        // 变动的配置
        private Set<Property> changedProperties;
        // 配置差异
        private PropertyDifference difference;
    }
}
