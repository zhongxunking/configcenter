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
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 配置value服务单元测试
 */
@Ignore
public class PropertyValueServiceTest extends AbstractTest {
    @Autowired
    private PropertyValueService propertyValueService;

    @Test
    public void testSetPropertyValue() {
        // 设置offline环境value
        String[] appIds = new String[]{"common", "core-domain", "account", "customer"};
        for (String appId : appIds) {
            SetPropertyValuesOrder order = new SetPropertyValuesOrder();
            order.setAppId(appId);
            order.setProfileId("offline");

            for (Scope scope : Scope.values()) {
                SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
                keyValue.setKey(String.format("%s-%s-key1", appId, scope.name().toLowerCase()));
                keyValue.setValue(String.format("%s-%s-value1-%s", appId, scope.name().toLowerCase(), "offline"));
                order.addKeyValue(keyValue);
            }

            EmptyResult result = propertyValueService.setPropertyValues(order);
            checkResult(result, Status.SUCCESS);
        }
        // 删除value
        for (String appId : appIds) {
            SetPropertyValuesOrder order = new SetPropertyValuesOrder();
            order.setAppId(appId);
            order.setProfileId("offline");

            SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
            keyValue.setKey(String.format("%s-%s-key1", appId, "public"));
            keyValue.setValue(null);
            order.addKeyValue(keyValue);

            EmptyResult result = propertyValueService.setPropertyValues(order);
            checkResult(result, Status.SUCCESS);
        }
        // 设置dev环境value
        for (String appId : appIds) {
            SetPropertyValuesOrder order = new SetPropertyValuesOrder();
            order.setAppId(appId);
            order.setProfileId("dev");

            for (Scope scope : Scope.values()) {
                if (scope == Scope.PRIVATE) {
                    continue;
                }
                SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
                keyValue.setKey(String.format("%s-%s-key1", appId, scope.name().toLowerCase()));
                keyValue.setValue(String.format("%s-%s-value1-%s", appId, scope.name().toLowerCase(), "dev"));
                order.addKeyValue(keyValue);
            }

            EmptyResult result = propertyValueService.setPropertyValues(order);
            checkResult(result, Status.SUCCESS);
        }
    }

    @Test
    public void testFindAppProfilePropertyValues() {
        FindAppProfilePropertyValuesOrder order = new FindAppProfilePropertyValuesOrder();
        order.setAppId("customer");
        order.setProfileId("dev");

        FindAppProfilePropertyValuesResult result = propertyValueService.findAppProfilePropertyValues(order);
        checkResult(result, Status.SUCCESS);
    }
}
