/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:23 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.FindInheritedAppPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindInheritedAppPropertyKeysResult;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.OperatePrivileges;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 配置key管理controller
 */
@RestController
@RequestMapping("/manage/propertyKey")
@AllArgsConstructor
public class PropertyKeyController {
    // 配置key服务
    private final PropertyKeyService propertyKeyService;

    /**
     * 新增或修改配置key
     *
     * @param appId 应用id
     * @param key   key
     * @param scope 作用域
     * @param memo  备注
     */
    @RequestMapping("/addOrModifyPropertyKey")
    public EmptyResult addOrModifyPropertyKey(String appId, String key, Scope scope, String memo) {
        ManagerApps.assertAdminOrHaveApp(appId);
        OperatePrivileges.assertAdminOrOnlyReadWrite(appId, Collections.singleton(key));
        AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
        order.setAppId(appId);
        order.setKey(key);
        order.setScope(scope);
        order.setMemo(memo);

        return propertyKeyService.addOrModifyPropertyKey(order);
    }

    /**
     * 删除配置key
     *
     * @param appId 应用id
     * @param key   key
     */
    @RequestMapping("/deletePropertyKey")
    public EmptyResult deletePropertyKey(String appId, String key) {
        ManagerApps.assertAdminOrHaveApp(appId);
        OperatePrivileges.assertAdminOrOnlyReadWrite(appId, Collections.singleton(key));
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppId(appId);
        order.setKey(key);

        return propertyKeyService.deletePropertyKey(order);
    }

    /**
     * 查找继承的应用配置key（包含应用自己）
     *
     * @param appId 应用id
     */
    @RequestMapping("/findInheritedAppPropertyKeys")
    public FindInheritedAppPropertyKeysResult findInheritedAppPropertyKeys(String appId) {
        ManagerApps.assertAdminOrHaveApp(appId);

        FindInheritedAppPropertyKeysOrder order = new FindInheritedAppPropertyKeysOrder();
        order.setAppId(appId);
        return propertyKeyService.findInheritedAppPropertyKeys(order);
    }
}
