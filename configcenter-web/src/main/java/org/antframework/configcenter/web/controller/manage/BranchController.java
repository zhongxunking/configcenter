/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-14 19:18 创建
 */
package org.antframework.configcenter.web.controller.manage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.json.JSON;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.PropertyValues;
import org.antframework.configcenter.facade.api.BranchService;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.MergenceDifference;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.FindBranchResult;
import org.antframework.configcenter.facade.result.FindBranchesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.OperatePrivileges;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.web.CurrentManagerAssert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
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
        ManagerApps.adminOrHaveApp(appId);
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
                                             String memo) throws JsonProcessingException {
        Set<Property> properties = convertToProperties(addOrModifiedProperties);
        Set<String> propertyKeys = convertToPropertyKeys(removedPropertyKeys);
        ManagerApps.adminOrHaveApp(appId);
        if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
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
            // 对敏感配置掩码
            if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
                maskRelease(result.getBranch().getRelease());
            }
        }
        return result;
    }

    // 转换出配置集
    private Set<Property> convertToProperties(String json) throws JsonProcessingException {
        Set<PropertyInfo> properties = JSON.OBJECT_MAPPER.readValue(json, new TypeReference<Set<PropertyInfo>>() {
        });
        return properties.stream()
                .map(propertyInfo -> new Property(propertyInfo.getKey(), propertyInfo.getValue(), propertyInfo.getScope()))
                .collect(Collectors.toSet());
    }

    // 转换出配置key集
    private Set<String> convertToPropertyKeys(String json) throws JsonProcessingException {
        return JSON.OBJECT_MAPPER.readValue(json, new TypeReference<Set<String>>() {
        });
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

        EmptyResult result = branchService.revertBranch(order);
        if (result.isSuccess()) {
            PropertyValues.revertPropertyValues(appId, profileId, branchId, targetReleaseVersion);
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
    public EmptyResult mergeBranch(String appId, String profileId, String branchId, String sourceBranchId) {
        ManagerApps.adminOrHaveApp(appId);
        MergenceDifference difference = Branches.computeBranchMergence(appId, profileId, branchId, sourceBranchId);
        MergeBranchOrder order = new MergeBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setSourceBranchId(sourceBranchId);

        EmptyResult result = branchService.mergeBranch(order);
        if (result.isSuccess()) {
            // 同步到配置value
            for (Property property : difference.getAddOrModifiedProperties()) {
                PropertyValues.addOrModifyPropertyValue(
                        appId,
                        profileId,
                        branchId,
                        property.getKey(),
                        property.getValue(),
                        property.getScope());
            }
            for (String key : difference.getRemovedPropertyKeys()) {
                PropertyValues.deletePropertyValue(appId, profileId, branchId, key);
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
        ManagerApps.adminOrHaveApp(appId);
        MergenceDifference difference = Branches.computeBranchMergence(appId, profileId, branchId, sourceBranchId);
        Map<String, Property> propertyMap = Branches.findBranch(appId, profileId, branchId)
                .getRelease()
                .getProperties()
                .stream()
                .collect(Collectors.toMap(Property::getKey, Function.identity()));

        ComputeBranchMergenceResult result = FacadeUtils.buildSuccess(ComputeBranchMergenceResult.class);
        for (Property addOrModifiedProperty : difference.getAddOrModifiedProperties()) {
            Property property = propertyMap.get(addOrModifiedProperty.getKey());
            if (Objects.equals(addOrModifiedProperty, property)) {
                continue;
            }
            result.addProperty(addOrModifiedProperty);
            if (property == null) {
                result.addAddedKeys(addOrModifiedProperty.getKey());
            } else {
                if (!Objects.equals(addOrModifiedProperty.getValue(), property.getValue())) {
                    result.addModifiedValueKey(addOrModifiedProperty.getKey());
                }
                if (addOrModifiedProperty.getScope() != property.getScope()) {
                    result.addModifiedScopeKey(addOrModifiedProperty.getKey());
                }
            }
        }
        difference.getRemovedPropertyKeys()
                .stream()
                .filter(propertyMap::containsKey)
                .forEach(key -> {
                    result.addProperty(propertyMap.get(key));
                    result.addRemovedKeys(key);
                });
        if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            Set<Property> maskedProperties = OperatePrivileges.maskProperties(appId, result.getProperties());
            result.getProperties().clear();
            result.getProperties().addAll(maskedProperties);
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
        ManagerApps.adminOrHaveApp(appId);
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
        Set<Property> maskedProperties = OperatePrivileges.maskProperties(release.getAppId(), release.getProperties());
        release.setProperties(maskedProperties);
    }

    // 配置项info
    @Getter
    @Setter
    private static class PropertyInfo {
        // key
        private String key;
        // value
        private String value;
        // 作用域
        private Scope scope;
    }

    /**
     * 计算分支合并result
     */
    @Getter
    public static class ComputeBranchMergenceResult extends AbstractResult {
        // 变动的配置
        private final Set<Property> properties = new HashSet<>();
        // 新增配置的key
        private final Set<String> addedKeys = new HashSet<>();
        // 被修改的配置value的key
        private final Set<String> modifiedValueKeys = new HashSet<>();
        // 被修改的scope的key
        private final Set<String> modifiedScopeKeys = new HashSet<>();
        // 被删除配置的key
        private final Set<String> removedKeys = new HashSet<>();

        public void addProperty(Property property) {
            properties.add(property);
        }

        public void addAddedKeys(String key) {
            addedKeys.add(key);
        }

        public void addModifiedValueKey(String key) {
            modifiedValueKeys.add(key);
        }

        public void addModifiedScopeKey(String key) {
            modifiedScopeKeys.add(key);
        }

        public void addRemovedKeys(String key) {
            removedKeys.add(key);
        }
    }
}
