/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-14 19:18 创建
 */
package org.antframework.configcenter.web.controller.manage;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.PropertyValues;
import org.antframework.configcenter.facade.api.BranchService;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.ComputeBranchMergenceResult;
import org.antframework.configcenter.facade.result.FindBranchResult;
import org.antframework.configcenter.facade.result.FindBranchesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.OperatePrivilege;
import org.antframework.configcenter.web.common.OperatePrivileges;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.web.CurrentManagers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分支controller
 */
@RestController
@RequestMapping("/manage/branch")
@AllArgsConstructor
public class BranchController {
    // 掩码后的配置value
    private static final String MASKED_VALUE = "******";

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
        ManagerApps.adminOrHaveApp(appId);
        AddBranchOrder order = new AddBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setReleaseVersion(releaseVersion);

        return branchService.addBranch(order);
    }

    /**
     * 发布分支
     *
     * @param appId                   应用id
     * @param profileId               环境id
     * @param branchId                分支id
     * @param addOrModifiedProperties 需添加或修改的配置
     * @param removedPropertyKeys     需删除的配置key
     * @param memo                    备注
     */
    @RequestMapping("/releaseBranch")
    public ReleaseBranchResult releaseBranch(String appId,
                                             String profileId,
                                             String branchId,
                                             String addOrModifiedProperties,
                                             String removedPropertyKeys,
                                             String memo) {
        Set<Property> properties = new HashSet<>(JSON.parseArray(addOrModifiedProperties, Property.class));
        Set<String> propertyKeys = new HashSet<>(JSON.parseArray(removedPropertyKeys, String.class));
        ManagerApps.adminOrHaveApp(appId);
        if (CurrentManagers.current().getType() != ManagerType.ADMIN) {
            // 校验是否有敏感配置被修改
            Set<String> keys = properties.stream().map(Property::getKey).collect(Collectors.toSet());
            keys.addAll(propertyKeys);
            OperatePrivileges.onlyReadWrite(appId, keys);
        }

        ReleaseBranchOrder order = new ReleaseBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setAddOrModifiedProperties(properties);
        order.setRemovedPropertyKeys(propertyKeys);
        order.setMemo(memo);

        ReleaseBranchResult result = branchService.releaseBranch(order);
        if (result.isSuccess()) {
            maskRelease(result.getBranch().getRelease());
            // 同步到配置value
            for (Property property : properties) {
                PropertyValues.addOrModifyPropertyValue(
                        appId,
                        profileId,
                        branchId,
                        property.getKey(),
                        property.getValue(),
                        property.getScope());
            }
            for (String key : propertyKeys) {
                PropertyValues.deletePropertyValue(appId, profileId, branchId, key);
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
    public EmptyResult revertBranch(String appId, String profileId, String branchId, Long targetReleaseVersion) {
        ManagerApps.adminOrHaveApp(appId);
        RevertBranchOrder order = new RevertBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setTargetReleaseVersion(targetReleaseVersion);

        return branchService.revertBranch(order);
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
    public EmptyResult mergeBranch(String appId, String profileId, String branchId, String sourceBranchId) {
        ManagerApps.adminOrHaveApp(appId);
        MergeBranchOrder order = new MergeBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setSourceBranchId(sourceBranchId);

        return branchService.mergeBranch(order);
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
        ManagerApps.adminOrHaveApp(appId);
        ComputeBranchMergenceOrder order = new ComputeBranchMergenceOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setSourceBranchId(sourceBranchId);

        ComputeBranchMergenceResult result = branchService.computeBranchMergence(order);
        if (result.isSuccess() && CurrentManagers.current().getType() != ManagerType.ADMIN) {
            Set<Property> maskedProperties = mask(appId, result.getDifference().getAddOrModifiedProperties());
            result.getDifference().getAddOrModifiedProperties().clear();
            result.getDifference().getAddOrModifiedProperties().addAll(maskedProperties);
        }
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
        ManagerApps.adminOrHaveApp(appId);
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
        ManagerApps.adminOrHaveApp(appId);
        FindBranchOrder order = new FindBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        FindBranchResult result = branchService.findBranch(order);
        if (result.isSuccess()) {
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
        ManagerApps.adminOrHaveApp(appId);
        FindBranchesOrder order = new FindBranchesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        FindBranchesResult result = branchService.findBranches(order);
        if (result.isSuccess()) {
            result.getBranches().stream().map(BranchInfo::getRelease).forEach(this::maskRelease);
        }
        return result;
    }

    // 对发布中敏感配置进行掩码
    private void maskRelease(ReleaseInfo release) {
        if (CurrentManagers.current().getType() != ManagerType.ADMIN) {
            Set<Property> maskedProperties = mask(release.getAppId(), release.getProperties());
            release.setProperties(maskedProperties);
        }
    }

    // 对敏感配置进行掩码
    private Set<Property> mask(String appId, Set<Property> properties) {
        List<OperatePrivileges.AppOperatePrivilege> appOperatePrivileges = OperatePrivileges.findInheritedOperatePrivileges(appId);
        Set<Property> maskedProperties = new HashSet<>(properties.size());
        for (Property property : properties) {
            OperatePrivilege privilege = OperatePrivileges.calcOperatePrivilege(appOperatePrivileges, property.getKey());
            if (privilege == OperatePrivilege.NONE) {
                maskedProperties.add(new Property(property.getKey(), MASKED_VALUE, property.getScope()));
            } else {
                maskedProperties.add(property);
            }
        }
        return maskedProperties;
    }
}
