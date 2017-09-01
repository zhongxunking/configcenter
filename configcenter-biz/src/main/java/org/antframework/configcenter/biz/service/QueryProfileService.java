/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:37 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.facade.order.manage.QueryProfileOrder;
import org.antframework.configcenter.facade.result.manage.QueryProfileResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service
public class QueryProfileService {
    @Autowired
    private ProfileDao profileDao;

    @ServiceExecute
    public void serviceExecute(ServiceContext<QueryProfileOrder, QueryProfileResult> serviceContext) {
        QueryProfileOrder order = serviceContext.getOrder();
    }

}
