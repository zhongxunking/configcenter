/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-04 12:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.biz.util.PropertyKeys;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.FindInheritedAppPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindInheritedAppPropertyKeysResult;
import org.antframework.configcenter.facade.vo.AppPropertyKey;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.List;

/**
 * 查找继承的应用配置key-service
 */
@Service
public class FindInheritedAppPropertyKeysService {
    @ServiceExecute
    public void execute(ServiceContext<FindInheritedAppPropertyKeysOrder, FindInheritedAppPropertyKeysResult> context) {
        FindInheritedAppPropertyKeysOrder order = context.getOrder();
        FindInheritedAppPropertyKeysResult result = context.getResult();

        for (AppInfo app : Apps.findInheritedApps(order.getAppId())) {
            List<PropertyKeyInfo> propertyKeys = PropertyKeys.findPropertyKeys(app.getAppId(), Scope.PRIVATE);
            result.addInheritedAppPropertyKey(new AppPropertyKey(app, propertyKeys));
        }
    }
}
