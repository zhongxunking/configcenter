/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:23 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.FindAppPropertyKeyOrder;
import org.antframework.configcenter.facade.order.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.FindAppPropertyKeyResult;
import org.antframework.configcenter.facade.result.QueryPropertyKeyResult;
import org.antframework.manager.web.common.ManagerAssert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性key管理controller
 */
@RestController
@RequestMapping("/manage/propertyKey")
public class PropertyKeyManageController {
    @Autowired
    private PropertyKeyService propertyKeyService;

    /**
     * 新增或修改属性key
     *
     * @param appId   应用id（必须）
     * @param key     key（必须）
     * @param outward 是否公用（必须）
     * @param memo    备注（可选）
     */
    @RequestMapping("/addOrModifyPropertyKey")
    public EmptyResult addOrModifyPropertyKey(String appId, String key, boolean outward, String memo) {
        ManagerAssert.adminOrHaveRelation(appId);
        AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
        order.setAppId(appId);
        order.setKey(key);
        order.setOutward(outward);
        order.setMemo(memo);

        return propertyKeyService.addOrModifyPropertyKey(order);
    }

    /**
     * 删除属性key
     *
     * @param appId 应用id（必须）
     * @param key   key（必须）
     */
    @RequestMapping("/deletePropertyKey")
    public EmptyResult deletePropertyKey(String appId, String key) {
        ManagerAssert.adminOrHaveRelation(appId);
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppId(appId);
        order.setKey(key);

        return propertyKeyService.deletePropertyKey(order);
    }

    /**
     * 查找应用所有的属性key
     *
     * @param appId 应用id（必须）
     */
    @RequestMapping("/findAppPropertyKey")
    public FindAppPropertyKeyResult findAppPropertyKey(String appId) {
        if (!StringUtils.equals(appId, ConfigService.COMMON_APP_ID)) {
            ManagerAssert.adminOrHaveRelation(appId);
        }
        FindAppPropertyKeyOrder order = new FindAppPropertyKeyOrder();
        order.setAppId(appId);

        return propertyKeyService.findAppPropertyKey(order);
    }

    /**
     * 分页查询属性key
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param appId    应用id（可选，有值会进行模糊查询）
     * @param key      key（可选，有值会进行模糊查询）
     * @param outward  是否公用（可选）
     */
    @RequestMapping("/queryPropertyKey")
    public QueryPropertyKeyResult queryPropertyKey(int pageNo, int pageSize, String appId, String key, Boolean outward) {
        ManagerAssert.admin();
        QueryPropertyKeyOrder order = new QueryPropertyKeyOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppId(appId);
        order.setKey(key);
        order.setOutward(outward);

        return propertyKeyService.queryPropertyKey(order);
    }
}
