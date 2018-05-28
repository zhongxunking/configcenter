/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 18:06 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.FindAppPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindAppPropertyKeysResult;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 属性key服务单元测试
 */
@Ignore
public class PropertyKeyServiceTest extends AbstractTest {
    @Autowired
    private PropertyKeyService propertyKeyService;

    @Test
    public void testAddOrModifyPropertyKey() {
        AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
        order.setAppId("scbfund");
        order.setKey("datasource.url");
        order.setScope(Scope.PRIVATE);
        order.setMemo("数据库地址");
        EmptyResult result = propertyKeyService.addOrModifyPropertyKey(order);
        checkResult(result, Status.SUCCESS);

        order = new AddOrModifyPropertyKeyOrder();
        order.setAppId("scbfund");
        order.setKey("collection.accNo");
        order.setScope(Scope.PROTECTED);
        order.setMemo("归集户帐号");
        result = propertyKeyService.addOrModifyPropertyKey(order);
        checkResult(result, Status.SUCCESS);

        order = new AddOrModifyPropertyKeyOrder();
        order.setAppId("scbfund");
        order.setKey("cashier.url");
        order.setScope(Scope.PUBLIC);
        order.setMemo("收银台地址");
        result = propertyKeyService.addOrModifyPropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testDeletePropertyKey() {
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppId("scbfund");
        order.setKey("collection.accNo");

        EmptyResult result = propertyKeyService.deletePropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindAppPropertyKeys() {
        FindAppPropertyKeysOrder order = new FindAppPropertyKeysOrder();
        order.setAppId("scbfund");

        FindAppPropertyKeysResult result = propertyKeyService.findAppPropertyKeys(order);
        checkResult(result, Status.SUCCESS);
    }
}
