/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 18:06 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.FindAppPropertyKeyOrder;
import org.antframework.configcenter.facade.order.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.FindAppPropertyKeyResult;
import org.antframework.configcenter.facade.result.QueryPropertyKeyResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Ignore
public class PropertyKeyServiceTest extends AbstractTest {
    @Autowired
    private PropertyKeyService propertyKeyService;

    @Test
    public void testAddOrModifyPropertyKey() {
        AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
        order.setAppId("scbfund");
        order.setKey("collection.accNo");
        order.setOutward(false);
        order.setMemo("归集户帐号");
        EmptyResult result = propertyKeyService.addOrModifyPropertyKey(order);
        checkResult(result, Status.SUCCESS);
        order = new AddOrModifyPropertyKeyOrder();
        order.setAppId("scbfund");
        order.setKey("cashier.url");
        order.setOutward(true);
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
    public void testFindAppPropertyKey() {
        FindAppPropertyKeyOrder order = new FindAppPropertyKeyOrder();
        order.setAppId("scbfund");
        FindAppPropertyKeyResult result = propertyKeyService.findAppPropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryPropertyKey() {
        QueryPropertyKeyOrder order = new QueryPropertyKeyOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        QueryPropertyKeyResult result = propertyKeyService.queryPropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }
}
