/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:38 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.order.QueryPropertyValuesOrder;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
import org.antframework.configcenter.facade.result.QueryPropertyValuesResult;
import org.antframework.manager.web.common.ManagerAssert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性value管理controller
 */
@RestController
@RequestMapping("/manage/propertyValue")
public class PropertyValueManageController {
    @Autowired
    private PropertyValueService propertyValueService;

    /**
     * 设置多个属性value
     *
     * @param appId     应用id（必须）
     * @param profileId 环境id（必须）
     * @param keys      一个或多个key（必须）
     * @param values    与keys数量对应的value（必须）
     */
    @RequestMapping("/setPropertyValue")
    public EmptyResult setPropertyValues(String appId, String profileId, String[] keys, String[] values) {
        if (keys.length != values.length) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "属性key和value数量不相等");
        }
        ManagerAssert.adminOrHaveRelation(appId);
        SetPropertyValuesOrder order = new SetPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        for (int i = 0; i < keys.length; i++) {
            SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
            keyValue.setKey(keys[i]);
            keyValue.setValue(values[i]);
            order.addKeyValue(keyValue);
        }

        return propertyValueService.setPropertyValues(order);
    }

    /**
     * 查找应用在指定环境的所有属性value
     *
     * @param appId     应用id（必须）
     * @param profileId 环境id（必须）
     */
    @RequestMapping("/findAppProfilePropertyValues")
    public FindAppProfilePropertyValuesResult findAppProfilePropertyValues(String appId, String profileId) {
        if (!StringUtils.equals(appId, ConfigService.COMMON_APP_ID)) {
            ManagerAssert.adminOrHaveRelation(appId);
        }
        FindAppProfilePropertyValuesOrder order = new FindAppProfilePropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        return propertyValueService.findAppProfilePropertyValues(order);
    }

    /**
     * 分页查询属性value
     *
     * @param pageNo    页码（必须）
     * @param pageSize  每页大小（必须）
     * @param appId     应用id（可选，有值会进行模糊查询）
     * @param key       key（可选，有值会进行模糊查询）
     * @param profileId 环境id（可选，有值会进行模糊查询）
     */
    @RequestMapping("/queryPropertyValues")
    public QueryPropertyValuesResult queryPropertyValues(int pageNo, int pageSize, String appId, String key, String profileId) {
        ManagerAssert.admin();
        QueryPropertyValuesOrder order = new QueryPropertyValuesOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppId(appId);
        order.setKey(key);
        order.setProfileId(profileId);

        return propertyValueService.queryPropertyValues(order);
    }
}
