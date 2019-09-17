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
import org.antframework.configcenter.facade.order.FindPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindPropertyKeysResult;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 配置key服务单元测试
 */
@Ignore
public class PropertyKeyServiceTest extends AbstractTest {
    @Autowired
    private PropertyKeyService propertyKeyService;

    @Test
    public void testAddOrModifyPropertyKey() {
        String[] appIds = new String[]{"common", "core-domain", "account", "customer"};
        for (String appId : appIds) {
            for (Scope scope : Scope.values()) {
                AddOrModifyPropertyKeyOrder order = new AddOrModifyPropertyKeyOrder();
                order.setAppId(appId);
                order.setKey(String.format("%s-%s-key1", appId, scope.name().toLowerCase()));
                order.setScope(scope);
                order.setMemo(String.format("%s-%s-key1的备注", appId, scope.name().toLowerCase()));
                EmptyResult result = propertyKeyService.addOrModifyPropertyKey(order);
                checkResult(result, Status.SUCCESS);
            }
        }
    }

    @Test
    public void testDeletePropertyKey() {
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppId("common");
        order.setKey("common-private-key1");

        EmptyResult result = propertyKeyService.deletePropertyKey(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindAppPropertyKeys() {
        FindPropertyKeysOrder order = new FindPropertyKeysOrder();
        order.setAppId("customer");
        order.setMinScope(Scope.PROTECTED);

        FindPropertyKeysResult result = propertyKeyService.findPropertyKeys(order);
        checkResult(result, Status.SUCCESS);
    }
}
