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
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
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
     * @param key       key
     * @param profileId 环境id
     * @param value     value
     * @param scope     作用域
     */
    public static void addOrModifyPropertyValue(String appId, String key, String profileId, String value, Scope scope) {
        AddOrModifyPropertyValueOrder order = new AddOrModifyPropertyValueOrder();
        order.setAppId(appId);
        order.setKey(key);
        order.setProfileId(profileId);
        order.setValue(value);
        order.setScope(scope);

        EmptyResult result = PROPERTY_VALUE_SERVICE.addOrModifyPropertyValue(order);
        FacadeUtils.assertSuccess(result);
    }

    /**
     * 删除配置value
     *
     * @param appId     应用id
     * @param key       配置key
     * @param profileId 环境id
     */
    public static void deletePropertyValue(String appId, String key, String profileId) {
        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setAppId(appId);
        order.setKey(key);
        order.setProfileId(profileId);

        EmptyResult result = PROPERTY_VALUE_SERVICE.deletePropertyValue(order);
        FacadeUtils.assertSuccess(result);
    }

    /**
     * 回滚配置value
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param releaseVersion 回滚到的目标发布版本
     */
    public static void revertPropertyValues(String appId, String profileId, Long releaseVersion) {
        RevertPropertyValuesOrder order = new RevertPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setReleaseVersion(releaseVersion);

        EmptyResult result = PROPERTY_VALUE_SERVICE.revertPropertyValues(order);
        FacadeUtils.assertSuccess(result);
    }

    /**
     * 查找应用在指定环境的所有配置value
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param minScope  最小作用域
     * @return 配置value
     */
    public static List<PropertyValueInfo> findAppProfilePropertyValues(String appId, String profileId, Scope minScope) {
        FindAppProfilePropertyValuesOrder order = new FindAppProfilePropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setMinScope(minScope);

        FindAppProfilePropertyValuesResult result = PROPERTY_VALUE_SERVICE.findAppProfilePropertyValues(order);
        FacadeUtils.assertSuccess(result);
        return result.getPropertyValues();
    }

    /**
     * 删除应用在指定环境的所有配置value
     *
     * @param appId     应用id
     * @param profileId 环境id
     */
    public static void deleteAppProfilePropertyValues(String appId, String profileId) {
        for (PropertyValueInfo propertyValue : findAppProfilePropertyValues(appId, profileId, Scope.PRIVATE)) {
            deletePropertyValue(propertyValue.getAppId(), propertyValue.getKey(), propertyValue.getProfileId());
        }
    }
}
