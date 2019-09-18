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
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.FindPropertyValuesOrder;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindPropertyValuesResult;
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
    public void testAddOrModifyPropertyValue() {
        String[] appIds = new String[]{"common", "core-domain", "account", "customer"};
        String[] profileIds = new String[]{"offline", "dev"};
        for (String appId : appIds) {
            for (String profileId : profileIds) {
                for (Scope scope : Scope.values()) {
                    AddOrModifyPropertyValueOrder order = new AddOrModifyPropertyValueOrder();
                    order.setAppId(appId);
                    order.setKey(String.format("%s-%s-key1", appId, scope.name().toLowerCase()));
                    order.setProfileId(profileId);
                    order.setValue(String.format("%s-%s-value1-%s", appId, scope.name().toLowerCase(), profileId));
                    order.setScope(scope);

                    EmptyResult result = propertyValueService.addOrModifyPropertyValue(order);
                    checkResult(result, Status.SUCCESS);
                }
            }
        }
    }

    @Test
    public void testDeletePropertyValue() {
        String[] appIds = new String[]{"common", "core-domain", "account", "customer"};
        for (String appId : appIds) {
            DeletePropertyValueOrder order = new DeletePropertyValueOrder();
            order.setAppId(appId);
            order.setKey(String.format("%s-%s-key1", appId, Scope.PRIVATE.name().toLowerCase()));
            order.setProfileId("dev");

            EmptyResult result = propertyValueService.deletePropertyValue(order);
            checkResult(result, Status.SUCCESS);
        }
    }

    @Test
    public void testRevertPropertyValues() {
        RevertPropertyValuesOrder order = new RevertPropertyValuesOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setReleaseVersion(0L);

        EmptyResult result = propertyValueService.revertPropertyValues(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindPropertyValues() {
        FindPropertyValuesOrder order = new FindPropertyValuesOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setMinScope(Scope.PRIVATE);

        FindPropertyValuesResult result = propertyValueService.findPropertyValues(order);
        checkResult(result, Status.SUCCESS);
    }
}
