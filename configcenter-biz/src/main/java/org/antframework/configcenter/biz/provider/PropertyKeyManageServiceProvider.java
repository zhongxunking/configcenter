/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:39 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.configcenter.facade.api.manage.PropertyKeyManageService;
import org.antframework.configcenter.facade.order.manage.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.FindAppPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyPropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.DeletePropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.FindAppPropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyKeyResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class PropertyKeyManageServiceProvider implements PropertyKeyManageService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public AddOrModifyPropertyKeyResult addOrModifyPropertyKey(AddOrModifyPropertyKeyOrder order) {
        return serviceEngine.execute("addOrModifyPropertyKeyService", order);
    }

    @Override
    public DeletePropertyKeyResult deletePropertyKey(DeletePropertyKeyOrder order) {
        return serviceEngine.execute("deletePropertyKeyService", order);
    }

    @Override
    public FindAppPropertyKeyResult findAppPropertyKey(FindAppPropertyKeyOrder order) {
        return serviceEngine.execute("findAppPropertyKeyService", order);
    }

    @Override
    public QueryPropertyKeyResult queryPropertyKey(QueryPropertyKeyOrder order) {
        return serviceEngine.execute("queryPropertyKeyService", order);
    }
}
