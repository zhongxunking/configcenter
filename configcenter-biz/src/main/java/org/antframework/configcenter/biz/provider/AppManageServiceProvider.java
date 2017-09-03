/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:59 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.configcenter.facade.api.manage.AppManageService;
import org.antframework.configcenter.facade.order.manage.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.order.manage.DeleteAppOrder;
import org.antframework.configcenter.facade.order.manage.QueryAppOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyAppResult;
import org.antframework.configcenter.facade.result.manage.DeleteAppResult;
import org.antframework.configcenter.facade.result.manage.QueryAppResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 应用管理服务提供者
 */
@Service
public class AppManageServiceProvider implements AppManageService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public AddOrModifyAppResult addOrModifyApp(AddOrModifyAppOrder order) {
        return serviceEngine.execute("addOrModifyAppService", order);
    }

    @Override
    public DeleteAppResult deleteApp(DeleteAppOrder order) {
        return serviceEngine.execute("deleteAppService", order);
    }

    @Override
    public QueryAppResult queryApp(QueryAppOrder order) {
        return serviceEngine.execute("queryAppService", order);
    }
}
