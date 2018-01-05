/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 12:41 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.boot.bekit.CommonQueryConstant;
import org.antframework.boot.bekit.CommonQueryResult;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.facade.order.QueryManagedAppOrder;
import org.antframework.configcenter.web.manager.facade.result.QueryManagedAppResult;
import org.bekit.service.ServiceEngine;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceCheck;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 查询被管理员管理的应用服务
 */
@Service
public class QueryManagedAppService {
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private ServiceEngine serviceEngine;

    @ServiceCheck
    public void check(ServiceContext<QueryManagedAppOrder, QueryManagedAppResult> context) {
        QueryManagedAppOrder order = context.getOrder();

        Manager manager = managerDao.findByManagerCode(order.getManagerCode());
        if (manager == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("管理员[%s]不存在", order.getManagerCode()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<QueryManagedAppOrder, QueryManagedAppResult> context) {
        QueryManagedAppOrder order = context.getOrder();
        QueryManagedAppResult result = context.getResult();

        CommonQueryResult commonQueryResult = serviceEngine.execute(CommonQueryConstant.SERVICE_NAME, order, QueryUtils.buildQueryAttachment(ManagerAppDao.class));
        commonQueryResult.convertTo(result, null);
    }
}
