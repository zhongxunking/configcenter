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
import org.antframework.configcenter.biz.util.PropertyValues;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.OperatePrivilege;
import org.antframework.configcenter.web.common.OperatePrivileges;
import org.antframework.configcenter.web.common.Properties;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.facade.info.ManagerInfo;
import org.antframework.manager.web.Managers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置value管理controller
 */
@RestController
@RequestMapping("/manage/propertyValue")
@AllArgsConstructor
public class PropertyValueController {
    // 掩码后的配置value
    private static final String MASKED_VALUE = "******";

    // 配置value服务
    private final PropertyValueService propertyValueService;

    /**
     * 新增或修改配置value
     *
     * @param appId     应用id（必须）
     * @param key       配置key（必须）
     * @param profileId 环境id（必须）
     * @param value     配置value（必须）
     * @param scope     作用域（必须）
     */
    @RequestMapping("/addOrModifyPropertyValue")
    public EmptyResult addOrModifyPropertyValue(String appId, String key, String profileId, String value, Scope scope) {
        ManagerApps.adminOrHaveApp(appId);
        OperatePrivileges.adminOrReadWrite(appId, key);

        AddOrModifyPropertyValueOrder order = new AddOrModifyPropertyValueOrder();
        order.setAppId(appId);
        order.setKey(key);
        order.setProfileId(profileId);
        order.setValue(value);
        order.setScope(scope);

        return propertyValueService.addOrModifyPropertyValue(order);
    }

    /**
     * 删除配置value
     *
     * @param appId     应用id（必须）
     * @param key       配置key（必须）
     * @param profileId 环境id（必须）
     */
    @RequestMapping("/deletePropertyValue")
    public EmptyResult deletePropertyValue(String appId, String key, String profileId) {
        ManagerApps.adminOrHaveApp(appId);
        OperatePrivileges.adminOrReadWrite(appId, key);

        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setAppId(appId);
        order.setKey(key);
        order.setProfileId(profileId);

        return propertyValueService.deletePropertyValue(order);
    }

    /**
     * 回滚配置value
     *
     * @param appId          应用id（必须）
     * @param profileId      环境id（必须）
     * @param releaseVersion 发布版本（必须）
     */
    @RequestMapping("/revertPropertyValues")
    public EmptyResult revertPropertyValues(String appId, String profileId, Long releaseVersion) {
        ManagerApps.adminOrHaveApp(appId);

        RevertPropertyValuesOrder order = new RevertPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setReleaseVersion(releaseVersion);

        return propertyValueService.revertPropertyValues(order);
    }

    /**
     * 查找应用在指定环境的所有配置value
     *
     * @param appId     应用id（必须）
     * @param profileId 环境id（必须）
     * @param minScope  最小作用域（必须）
     */
    @RequestMapping("/findAppProfilePropertyValues")
    public FindAppProfilePropertyValuesResult findAppProfilePropertyValues(String appId, String profileId, Scope minScope) {
        ManagerApps.adminOrHaveApp(appId);

        FindAppProfilePropertyValuesOrder order = new FindAppProfilePropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setMinScope(minScope);

        FindAppProfilePropertyValuesResult result = propertyValueService.findAppProfilePropertyValues(order);
        if (result.isSuccess()) {
            mask(appId, result.getPropertyValues());
        }
        return result;
    }

    // 对敏感配置进行掩码
    private void mask(String appId, List<PropertyValueInfo> propertyValues) {
        ManagerInfo manager = Managers.currentManager();
        if (manager.getType() == ManagerType.ADMIN) {
            return;
        }
        List<OperatePrivileges.AppPrivilege> appPrivileges = OperatePrivileges.findInheritedOperatePrivileges(appId);
        for (PropertyValueInfo propertyValue : propertyValues) {
            OperatePrivilege privilege = OperatePrivileges.calcOperatePrivilege(appPrivileges, propertyValue.getKey());
            if (privilege == OperatePrivilege.NONE) {
                propertyValue.setValue(MASKED_VALUE);
            }
        }
    }

    /**
     * 比较配置value与发布的差异
     *
     * @param appId          应用id（必须）
     * @param profileId      环境id（必须）
     * @param releaseVersion 发布版本（必须）
     */
    @RequestMapping("/comparePropertyValuesWithRelease")
    public ComparePropertyValuesWithReleaseResult comparePropertyValuesWithRelease(String appId, String profileId, Long releaseVersion) {
        ManagerApps.adminOrHaveApp(appId);

        List<PropertyValueInfo> propertyValues = PropertyValues.findAppProfilePropertyValues(appId, profileId, Scope.PRIVATE);
        List<Property> left = propertyValues.stream().map(propertyValue -> new Property(propertyValue.getKey(), propertyValue.getValue(), propertyValue.getScope())).collect(Collectors.toList());
        List<Property> right = Releases.findRelease(appId, profileId, releaseVersion).getProperties();
        Properties.Difference difference = Properties.compare(left, right);

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
        private Properties.Difference difference;
    }
}
