/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 20:38 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.manage.PropertyValueManageService;
import org.antframework.configcenter.facade.order.manage.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.SetPropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.DeletePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;
import org.antframework.configcenter.facade.result.manage.SetPropertyValueResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Ignore
public class PropertyValueManageServiceTest extends AbstractTest {
    @Autowired
    private PropertyValueManageService propertyValueManageService;

    @Test
    public void testSetPropertyValue() {
        SetPropertyValueOrder order = new SetPropertyValueOrder();
        order.setProfileCode("dev");
        order.setAppCode("scbfund");
        order.setKey("collection.accNo");
        order.setValue("20170903200000000001");
        SetPropertyValueResult result = propertyValueManageService.setPropertyValue(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void tesDeletePropertyValue() {
        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setProfileCode("dev");
        order.setAppCode("scbfund");
        order.setKey("collection.accNo");
        DeletePropertyValueResult result = propertyValueManageService.deletePropertyValue(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryPropertyValue() {
        QueryPropertyValueOrder order = new QueryPropertyValueOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        QueryPropertyValueResult result = propertyValueManageService.queryPropertyValue(order);
        checkResult(result, Status.SUCCESS);
        Assert.assertEquals(1, result.getInfos().size());
    }
}
