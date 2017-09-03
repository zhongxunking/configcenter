/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 18:06 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.manage.PropertyKeyManageService;
import org.antframework.configcenter.facade.order.manage.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyPropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.DeletePropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyKeyResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class PropertyKeyManageServiceTest extends AbstractTest {
    @Autowired
    private PropertyKeyManageService propertyKeyManageService;

    @Test
    public void testAddOrModifyPropertyKey() {
        AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
        order.setAppCode("scbfund");
        order.setKey("collection.accNo");
        order.setCommon(false);
        order.setMemo("归集户帐号");
        AddOrModifyPropertyKeyResult result = propertyKeyManageService.addOrModifyPropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testDeletePropertyKey() {
        testAddOrModifyPropertyKey();
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppCode("scbfund");
        order.setKey("collection.accNo");
        DeletePropertyKeyResult result = propertyKeyManageService.deletePropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryPropertyKey() {
        testAddOrModifyPropertyKey();
        QueryPropertyKeyOrder order = new QueryPropertyKeyOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        QueryPropertyKeyResult result = propertyKeyManageService.queryPropertyKey(order);
        checkResult(result, Status.SUCCESS);
        Assert.assertEquals(1, result.getInfos().size());
    }
}
