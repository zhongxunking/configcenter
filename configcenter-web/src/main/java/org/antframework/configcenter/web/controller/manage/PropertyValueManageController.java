/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:38 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.configcenter.facade.api.manage.PropertyValueManageService;
import org.antframework.configcenter.facade.order.manage.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.FindAppProfilePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.SetPropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.DeletePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.FindAppProfilePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;
import org.antframework.configcenter.facade.result.manage.SetPropertyValueResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性value管理controller
 */
@RestController
@RequestMapping("/manage/propertyValue")
public class PropertyValueManageController extends AbstractController {
    @Autowired
    private PropertyValueManageService propertyValueManageService;

    /**
     * 设置属性value
     *
     * @param profileCode 环境编码（必须）
     * @param appCode     应用编码（必须）
     * @param key         key（必须）
     * @param value       value（必须）
     */
    @RequestMapping("/setPropertyValue")
    public SetPropertyValueResult setPropertyValue(String profileCode, String appCode, String key, String value) {
        canModifyApp(appCode);
        SetPropertyValueOrder order = new SetPropertyValueOrder();
        order.setProfileCode(profileCode);
        order.setAppCode(appCode);
        order.setKey(key);
        order.setValue(value);

        return propertyValueManageService.setPropertyValue(order);
    }

    /**
     * 删除属性value
     *
     * @param profileCode 环境编码（必须）
     * @param appCode     应用编码（必须）
     * @param key         key（必须）
     */
    @RequestMapping("/deletePropertyValue")
    public DeletePropertyValueResult deletePropertyValue(String profileCode, String appCode, String key) {
        canModifyApp(appCode);
        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setProfileCode(profileCode);
        order.setAppCode(appCode);
        order.setKey(key);

        return propertyValueManageService.deletePropertyValue(order);
    }

    /**
     * 查找应用在指定环境的所有属性value
     *
     * @param appCode     应用编码（必须）
     * @param profileCode 环境编码（必须）
     */
    @RequestMapping("/findAppProfilePropertyValue")
    public FindAppProfilePropertyValueResult findAppProfilePropertyValue(String appCode, String profileCode) {
        canReadApp(appCode);
        FindAppProfilePropertyValueOrder order = new FindAppProfilePropertyValueOrder();
        order.setAppCode(appCode);
        order.setProfileCode(profileCode);

        return propertyValueManageService.findAppProfilePropertyValue(order);
    }

    /**
     * 分页查询属性value
     *
     * @param pageNo      页码（必须）
     * @param pageSize    每页大小（必须）
     * @param profileCode 环境编码（可选，有值会进行模糊查询）
     * @param appCode     应用编码（可选，有值会进行模糊查询）
     * @param key         key（可选，有值会进行模糊查询）
     */
    @RequestMapping("/queryPropertyValue")
    public QueryPropertyValueResult queryPropertyValue(int pageNo, int pageSize, String profileCode, String appCode, String key) {
        canModifyApp(appCode);
        QueryPropertyValueOrder order = new QueryPropertyValueOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setProfileCode(profileCode);
        order.setAppCode(appCode);
        order.setKey(key);

        return propertyValueManageService.queryPropertyValue(order);
    }
}
