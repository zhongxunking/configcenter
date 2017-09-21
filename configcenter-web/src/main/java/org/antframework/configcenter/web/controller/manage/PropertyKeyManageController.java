/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:23 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.configcenter.facade.api.manage.PropertyKeyManageService;
import org.antframework.configcenter.facade.order.manage.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.FindAppPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyPropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.DeletePropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.FindAppPropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyKeyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性key管理controller
 */
@RestController
@RequestMapping("/manage/propertyKey")
public class PropertyKeyManageController extends AbstractController {
    @Autowired
    private PropertyKeyManageService propertyKeyManageService;

    /**
     * 新增或修改属性key
     *
     * @param appCode 应用编码（必须）
     * @param key     key（必须）
     * @param outward 是否公用（必须）
     * @param memo    备注（可选）
     */
    @RequestMapping("/addOrModifyPropertyKey")
    public AddOrModifyPropertyKeyResult addOrModifyPropertyKey(String appCode, String key, boolean outward, String memo) {
        canModifyApp(appCode);
        AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
        order.setAppCode(appCode);
        order.setKey(key);
        order.setOutward(outward);
        order.setMemo(memo);

        return propertyKeyManageService.addOrModifyPropertyKey(order);
    }

    /**
     * 删除属性key
     *
     * @param appCode 应用编码（必须）
     * @param key     key（必须）
     */
    @RequestMapping("/deletePropertyKey")
    public DeletePropertyKeyResult deletePropertyKey(String appCode, String key) {
        canModifyApp(appCode);
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppCode(appCode);
        order.setKey(key);

        return propertyKeyManageService.deletePropertyKey(order);
    }

    /**
     * 查找应用所有的属性key
     *
     * @param appCode 应用编码（必须）
     */
    @RequestMapping("/findAppPropertyKey")
    public FindAppPropertyKeyResult findAppPropertyKey(String appCode) {
        canReadApp(appCode);
        FindAppPropertyKeyOrder order = new FindAppPropertyKeyOrder();
        order.setAppCode(appCode);

        return propertyKeyManageService.findAppPropertyKey(order);
    }

    /**
     * 分页查询属性key
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param appCode  应用编码（可选，有值会进行模糊查询）
     * @param key      key（可选，有值会进行模糊查询）
     * @param outward  是否公用（可选，有值会进行模糊查询）
     */
    @RequestMapping("/queryPropertyKey")
    public QueryPropertyKeyResult queryPropertyKey(int pageNo, int pageSize, String appCode, String key, Boolean outward) {
        assertAdmin();
        QueryPropertyKeyOrder order = new QueryPropertyKeyOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppCode(appCode);
        order.setKey(key);
        order.setOutward(outward);

        return propertyKeyManageService.queryPropertyKey(order);
    }
}
