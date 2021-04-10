/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:38 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.biz.util.Properties;
import org.antframework.configcenter.biz.util.PropertyValues;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.PropertyDifference;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.FindPropertyValuesOrder;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindPropertyValuesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.AppPropertyTypes;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.web.CurrentManagerAssert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 配置value管理controller
 */
@RestController
@RequestMapping("/manage/propertyValue")
@AllArgsConstructor
public class PropertyValueController {
    // 配置value服务
    private final PropertyValueService propertyValueService;

    /**
     * 新增或修改配置value
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     * @param key       key
     * @param value     value
     * @param scope     作用域
     */
    @RequestMapping("/addOrModifyPropertyValue")
    public EmptyResult addOrModifyPropertyValue(String appId, String profileId, String branchId, String key, String value, Scope scope) {
        ManagerApps.assertAdminOrHaveApp(appId);
        AppPropertyTypes.assertAdminOrOnlyReadWrite(appId, Collections.singleton(key));

        AddOrModifyPropertyValueOrder order = new AddOrModifyPropertyValueOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setKey(key);
        order.setValue(value);
        order.setScope(scope);

        return propertyValueService.addOrModifyPropertyValue(order);
    }

    /**
     * 删除配置value
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     * @param key       key
     */
    @RequestMapping("/deletePropertyValue")
    public EmptyResult deletePropertyValue(String appId, String profileId, String branchId, String key) {
        ManagerApps.assertAdminOrHaveApp(appId);
        AppPropertyTypes.assertAdminOrOnlyReadWrite(appId, Collections.singleton(key));

        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setKey(key);

        return propertyValueService.deletePropertyValue(order);
    }

    /**
     * 回滚配置value
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param releaseVersion 发布版本
     */
    @RequestMapping("/revertPropertyValues")
    public EmptyResult revertPropertyValues(String appId, String profileId, String branchId, Long releaseVersion) {
        ManagerApps.assertAdminOrHaveApp(appId);

        RevertPropertyValuesOrder order = new RevertPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setReleaseVersion(releaseVersion);

        return propertyValueService.revertPropertyValues(order);
    }

    /**
     * 查找配置value集
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     * @param minScope  最小作用域
     */
    @RequestMapping("/findPropertyValues")
    public FindPropertyValuesResult findPropertyValues(String appId, String profileId, String branchId, Scope minScope) {
        ManagerApps.assertAdminOrHaveApp(appId);

        FindPropertyValuesOrder order = new FindPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setMinScope(minScope);

        FindPropertyValuesResult result = propertyValueService.findPropertyValues(order);
        if (result.isSuccess() && CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            List<PropertyValueInfo> maskedPropertyValues = AppPropertyTypes.maskPropertyValues(appId, result.getPropertyValues());
            result.getPropertyValues().clear();
            result.getPropertyValues().addAll(maskedPropertyValues);
        }
        return result;
    }

    /**
     * 比较配置value与发布的差异
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param releaseVersion 发布版本
     */
    @RequestMapping("/comparePropertyValuesWithRelease")
    public ComparePropertyValuesWithReleaseResult comparePropertyValuesWithRelease(String appId, String profileId, String branchId, Long releaseVersion) {
        ManagerApps.assertAdminOrHaveApp(appId);

        List<PropertyValueInfo> propertyValues = PropertyValues.findPropertyValues(appId, profileId, branchId, Scope.PRIVATE);
        Set<Property> left = propertyValues.stream().map(propertyValue -> new Property(propertyValue.getKey(), propertyValue.getValue(), propertyValue.getScope())).collect(Collectors.toSet());
        Set<Property> right = Releases.findRelease(appId, profileId, releaseVersion).getProperties();
        PropertyDifference difference = Properties.compare(left, right);

        ComparePropertyValuesWithReleaseResult result = FacadeUtils.buildSuccess(ComparePropertyValuesWithReleaseResult.class);
        result.setDifference(difference);
        return result;
    }

    /**
     * 比较配置value与发布的差异--result
     */
    @Getter
    @Setter
    public static class ComparePropertyValuesWithReleaseResult extends AbstractResult {
        // 差异
        private PropertyDifference difference;
    }
}
