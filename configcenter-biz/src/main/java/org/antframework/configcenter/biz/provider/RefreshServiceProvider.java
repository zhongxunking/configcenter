/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 13:55 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.configcenter.facade.api.manage.RefreshService;
import org.antframework.configcenter.facade.order.manage.SyncDataToZkOrder;
import org.antframework.configcenter.facade.order.manage.TriggerClientRefreshOrder;
import org.antframework.configcenter.facade.result.manage.SyncDataToZkResult;
import org.antframework.configcenter.facade.result.manage.TriggerClientRefreshResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 刷新服务提供者
 */
@Service
public class RefreshServiceProvider implements RefreshService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public SyncDataToZkResult syncDataToZk(SyncDataToZkOrder order) {
        return serviceEngine.execute("syncDataToZkService", order);
    }

    @Override
    public TriggerClientRefreshResult triggerClientRefresh(TriggerClientRefreshOrder order) {
        return serviceEngine.execute("triggerClientRefreshService", order);
    }
}
