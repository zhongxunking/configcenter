/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:38 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.PropertyValueUtils;
import org.antframework.configcenter.biz.util.ReleaseUtils;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.KeyPrivileges;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.Privilege;
import org.antframework.configcenter.web.util.PropertyAnalyzer;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.facade.info.ManagerInfo;
import org.antframework.manager.web.Managers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置value管理controller
 */
@RestController
@RequestMapping("/manage/propertyValue")
public class PropertyValueController {
    // 掩码后的配置value
    private static final String MASKED_VALUE = "******";
    @Autowired
    private PropertyValueService propertyValueService;

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
        KeyPrivileges.adminOrReadWrite(appId, key);

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
        KeyPrivileges.adminOrReadWrite(appId, key);

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
     */
    @RequestMapping("/findAppProfileCurrentPropertyValues")
    public FindAppProfileCurrentPropertyValuesResult findAppProfileCurrentPropertyValues(String appId, String profileId) {
        ManagerApps.adminOrHaveApp(appId);

        List<PropertyValueInfo> propertyValues = PropertyValueUtils.findAppProfilePropertyValues(appId, profileId, Scope.PRIVATE);
        ReleaseInfo release = ReleaseUtils.findCurrentRelease(appId, profileId);

        FindAppProfileCurrentPropertyValuesResult result = new FindAppProfileCurrentPropertyValuesResult();
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
        result.getPropertyValues().addAll(propertyValues);
        result.setChangedProperties(PropertyAnalyzer.analyzeChangedPropretyValues(release.getProperties(), propertyValues));
        // 掩码
        maskPropertyValue(appId, result);

        return result;
    }

    // 对敏感配置进行掩码
    private void maskPropertyValue(String appId, FindAppProfileCurrentPropertyValuesResult result) {
        ManagerInfo manager = Managers.currentManager();
        if (manager.getType() == ManagerType.ADMIN) {
            return;
        }
        List<KeyPrivileges.AppPrivilege> appPrivileges = KeyPrivileges.findInheritedPrivileges(appId);
        for (PropertyValueInfo propertyValue : result.getPropertyValues()) {
            Privilege privilege = KeyPrivileges.calcPrivilege(appPrivileges, propertyValue.getKey());
            if (privilege == Privilege.NONE) {
                propertyValue.setValue(MASKED_VALUE);
            }
        }
    }

    /**
     * 查找应用在指定环境下的配置value
     */
    @Getter
    @Setter
    public static class FindAppProfileCurrentPropertyValuesResult extends AbstractResult {
        // 配置value
        private List<PropertyValueInfo> propertyValues = new ArrayList<>();
        // 被修改的配置
        private PropertyAnalyzer.ChangedProperties changedProperties;
    }
}
