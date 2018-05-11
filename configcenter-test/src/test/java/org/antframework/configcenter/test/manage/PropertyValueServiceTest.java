/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 20:38 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.order.QueryPropertyValuesOrder;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
import org.antframework.configcenter.facade.result.QueryPropertyValuesResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Ignore
public class PropertyValueServiceTest extends AbstractTest {
    @Autowired
    private PropertyValueService propertyValueService;

    @Test
    public void testSetPropertyValue() {
        SetPropertyValuesOrder order = new SetPropertyValuesOrder();
        order.setAppId("scbfund");
        order.setProfileId("dev");
        SetPropertyValuesOrder.KeyValue keyValue1 = new SetPropertyValuesOrder.KeyValue();
        keyValue1.setKey("collection.accNo");
        keyValue1.setValue("20170903200000000001");
        order.addKeyValue(keyValue1);
        SetPropertyValuesOrder.KeyValue keyValue2 = new SetPropertyValuesOrder.KeyValue();
        keyValue2.setKey("cashier.url");
        keyValue2.setValue("http://localhost:8080/cashier");
        order.addKeyValue(keyValue2);
        EmptyResult result = propertyValueService.setPropertyValues(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void tesDeletePropertyValue() {
        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setAppId("scbfund");
        order.setKey("collection.accNo");
        order.setProfileId("dev");
        EmptyResult result = propertyValueService.deletePropertyValue(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindAppProfilePropertyValues() {
        FindAppProfilePropertyValuesOrder order = new FindAppProfilePropertyValuesOrder();
        order.setAppId("scbfund");
        order.setProfileId("dev");
        FindAppProfilePropertyValuesResult result = propertyValueService.findAppProfilePropertyValues(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryPropertyValues() {
        QueryPropertyValuesOrder order = new QueryPropertyValuesOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        QueryPropertyValuesResult result = propertyValueService.queryPropertyValues(order);
        checkResult(result, Status.SUCCESS);
    }
}
