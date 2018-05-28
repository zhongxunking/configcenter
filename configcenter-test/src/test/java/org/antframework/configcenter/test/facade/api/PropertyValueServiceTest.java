/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 20:38 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 属性value服务单元测试
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
        keyValue1.setKey("datasource.url");
        keyValue1.setValue("jdbc:mysql://localhost:3306/scbfund-dev");
        order.addKeyValue(keyValue1);

        SetPropertyValuesOrder.KeyValue keyValue2 = new SetPropertyValuesOrder.KeyValue();
        keyValue2.setKey("collection.accNo");
        keyValue2.setValue("20170903200000000001");
        order.addKeyValue(keyValue2);

        SetPropertyValuesOrder.KeyValue keyValue3 = new SetPropertyValuesOrder.KeyValue();
        keyValue3.setKey("cashier.url");
        keyValue3.setValue("http://localhost:8080/cashier");
        order.addKeyValue(keyValue3);

        EmptyResult result = propertyValueService.setPropertyValues(order);
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
}
