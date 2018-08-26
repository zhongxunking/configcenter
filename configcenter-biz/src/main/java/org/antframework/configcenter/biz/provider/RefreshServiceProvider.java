/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 13:55 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.api.RefreshService;
import org.antframework.configcenter.facade.order.RefreshClientsOrder;
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
    public EmptyResult refreshZk(EmptyOrder order) {
        return serviceEngine.execute("refreshZkService", order);
    }

    @Override
    public EmptyResult refreshClients(RefreshClientsOrder order) {
        return serviceEngine.execute("refreshClientsService", order);
    }
}
