/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:27 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.facade.order.manage.QueryAppOrder;
import org.antframework.configcenter.facade.result.manage.QueryAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 *
 */
@Service
public class QueryAppService {

    @ServiceExecute
    public void serviceExecute(ServiceContext<QueryAppOrder, QueryAppResult> serviceContext) {
        QueryAppOrder order = serviceContext.getOrder();

    }

}
