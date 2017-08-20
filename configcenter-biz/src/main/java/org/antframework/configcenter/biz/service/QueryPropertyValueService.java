/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:38 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service
public class QueryPropertyValueService {
    @Autowired
    private PropertyValueDao propertyValueDao;

    @ServiceExecute
    public void serviceExecute(ServiceContext<QueryPropertyValueOrder, QueryPropertyValueResult> serviceContext) {
        QueryPropertyValueOrder order = serviceContext.getOrder();

    }

}
