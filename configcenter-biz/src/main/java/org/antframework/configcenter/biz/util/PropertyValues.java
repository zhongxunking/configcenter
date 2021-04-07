/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 23:41 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.PropertyChange;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.FindPropertyValuesOrder;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindPropertyValuesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;

import java.util.List;

/**
 * 配置value操作类
 */
public final class PropertyValues {
    // 配置value服务
    private static final PropertyValueService PROPERTY_VALUE_SERVICE = Contexts.getApplicationContext().getBean(PropertyValueService.class);

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
    public static void addOrModifyPropertyValue(String appId,
                                                String profileId,
                                                String branchId,
                                                String key,
                                                String value,
                                                Scope scope) {
        AddOrModifyPropertyValueOrder order = new AddOrModifyPropertyValueOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setKey(key);
        order.setValue(value);
        order.setScope(scope);

        EmptyResult result = PROPERTY_VALUE_SERVICE.addOrModifyPropertyValue(order);
        FacadeUtils.assertSuccess(result);
    }

    /**
     * 删除配置value
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     * @param key       key
     */
    public static void deletePropertyValue(String appId,
                                           String profileId,
                                           String branchId,
                                           String key) {
        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setKey(key);

        EmptyResult result = PROPERTY_VALUE_SERVICE.deletePropertyValue(order);
        FacadeUtils.assertSuccess(result);
    }

    /**
     * 删除应用在环境的分支下所有配置value
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     */
    public static void deletePropertyValues(String appId, String profileId, String branchId) {
        for (PropertyValueInfo propertyValue : findPropertyValues(appId, profileId, branchId, Scope.PRIVATE)) {
            deletePropertyValue(propertyValue.getAppId(), propertyValue.getProfileId(), propertyValue.getBranchId(), propertyValue.getKey());
        }
    }

    /**
     * 修改配置value
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param propertyChange 配置变动
     */
    public static void changePropertyValues(String appId,
                                            String profileId,
                                            String branchId,
                                            PropertyChange propertyChange) {
        for (Property property : propertyChange.getAddedOrModifiedProperties()) {
            addOrModifyPropertyValue(
                    appId,
                    profileId,
                    branchId,
                    property.getKey(),
                    property.getValue(),
                    property.getScope());
        }
        for (String key : propertyChange.getDeletedKeys()) {
            deletePropertyValue(appId, profileId, branchId, key);
        }
    }

    /**
     * 回滚配置value
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param releaseVersion 回滚到的目标发布版本
     */
    public static void revertPropertyValues(String appId,
                                            String profileId,
                                            String branchId,
                                            Long releaseVersion) {
        RevertPropertyValuesOrder order = new RevertPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setReleaseVersion(releaseVersion);

        EmptyResult result = PROPERTY_VALUE_SERVICE.revertPropertyValues(order);
        FacadeUtils.assertSuccess(result);
    }

    /**
     * 查找应用在指定环境的所有配置value
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     * @param minScope  最小作用域
     * @return 配置value
     */
    public static List<PropertyValueInfo> findPropertyValues(String appId,
                                                             String profileId,
                                                             String branchId,
                                                             Scope minScope) {
        FindPropertyValuesOrder order = new FindPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setMinScope(minScope);

        FindPropertyValuesResult result = PROPERTY_VALUE_SERVICE.findPropertyValues(order);
        FacadeUtils.assertSuccess(result);
        return result.getPropertyValues();
    }
}
