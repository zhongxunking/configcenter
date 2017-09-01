/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:00 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.facade.order.manage.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.QueryPropertyKeyResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service
public class QueryPropertyKeyService {
    @Autowired
    private PropertyKeyDao propertyKeyDao;

    @ServiceExecute
    public void serviceExecute(ServiceContext<QueryPropertyKeyOrder, QueryPropertyKeyResult> serviceContext) {
        QueryPropertyKeyOrder order = serviceContext.getOrder();


    }

}
