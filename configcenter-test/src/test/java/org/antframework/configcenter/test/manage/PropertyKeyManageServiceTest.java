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
import org.antframework.configcenter.facade.api.manage.PropertyKeyManageService;
import org.antframework.configcenter.facade.order.manage.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.FindAppPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.FindAppPropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyKeyResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Ignore
public class PropertyKeyManageServiceTest extends AbstractTest {
    @Autowired
    private PropertyKeyManageService propertyKeyManageService;

    @Test
    public void testAddOrModifyPropertyKey() {
        AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
        order.setAppCode("scbfund");
        order.setKey("collection.accNo");
        order.setOutward(false);
        order.setMemo("归集户帐号");
        EmptyResult result = propertyKeyManageService.addOrModifyPropertyKey(order);
        checkResult(result, Status.SUCCESS);
        order = new AddOrModifyPropertyKeyOrder();
        order.setAppCode("scbfund");
        order.setKey("cashier.url");
        order.setOutward(true);
        order.setMemo("收银台地址");
        result = propertyKeyManageService.addOrModifyPropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testDeletePropertyKey() {
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppCode("scbfund");
        order.setKey("collection.accNo");
        EmptyResult result = propertyKeyManageService.deletePropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindAppPropertyKey() {
        FindAppPropertyKeyOrder order = new FindAppPropertyKeyOrder();
        order.setAppCode("scbfund");
        FindAppPropertyKeyResult result = propertyKeyManageService.findAppPropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryPropertyKey() {
        QueryPropertyKeyOrder order = new QueryPropertyKeyOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        QueryPropertyKeyResult result = propertyKeyManageService.queryPropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }
}
