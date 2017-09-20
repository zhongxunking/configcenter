/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:34 创建
 */
package org.antframework.configcenter.web.manager.biz.provider;

import org.antframework.configcenter.web.manager.facade.api.ManagerAppManageService;
import org.antframework.configcenter.web.manager.facade.order.AddManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.order.DeleteManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.order.QueryManagedAppOrder;
import org.antframework.configcenter.web.manager.facade.order.QueryManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.result.AddManagerAppResult;
import org.antframework.configcenter.web.manager.facade.result.DeleteManagerAppResult;
import org.antframework.configcenter.web.manager.facade.result.QueryManagedAppResult;
import org.antframework.configcenter.web.manager.facade.result.QueryManagerAppResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员关联应用管理服务提供者
 */
@Service
public class ManagerAppManageServiceProvider implements ManagerAppManageService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public AddManagerAppResult addManagerApp(AddManagerAppOrder order) {
        return serviceEngine.execute("addManagerAppService", order);
    }

    @Override
    public DeleteManagerAppResult deleteManagerApp(DeleteManagerAppOrder order) {
        return serviceEngine.execute("deleteManagerAppService", order);
    }

    @Override
    public QueryManagedAppResult queryManagedApp(QueryManagedAppOrder order) {
        return serviceEngine.execute("queryManagedAppService", order);
    }

    @Override
    public QueryManagerAppResult queryManagerApp(QueryManagerAppOrder order) {
        return serviceEngine.execute("queryManagerAppService", order);
    }
}
