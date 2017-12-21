/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 12:41 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.jpa.query.QueryOperator;
import org.antframework.common.util.jpa.query.QueryParam;
import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.dal.entity.ManagerApp;
import org.antframework.configcenter.web.manager.facade.order.QueryManagedAppOrder;
import org.antframework.configcenter.web.manager.facade.result.QueryManagedAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceCheck;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 查询被管理员管理的应用服务
 */
@Service
public class QueryManagedAppService {
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private ManagerAppDao managerAppDao;

    @ServiceCheck
    public void check(ServiceContext<QueryManagedAppOrder, QueryManagedAppResult> context) {
        QueryManagedAppOrder order = context.getOrder();

        Manager manager = managerDao.findByManagerCode(order.getManagerCode());
        if (manager == null) {
            throw new AntBekitException(Status.SUCCESS, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("管理员[%s]不存在", order.getManagerCode()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<QueryManagedAppOrder, QueryManagedAppResult> context) {
        QueryManagedAppOrder order = context.getOrder();

        Page<ManagerApp> page = managerAppDao.query(buildQueryParams(order), buildPageable(order));
        FacadeUtils.setQueryResult(context.getResult(), new FacadeUtils.SpringDataPageExtractor<>(page));
    }

    // 构建queryParams
    private Collection<QueryParam> buildQueryParams(QueryManagedAppOrder queryManagedAppOrder) {
        Collection<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam("managerCode", QueryOperator.EQ, queryManagedAppOrder.getManagerCode()));
        if (queryManagedAppOrder.getAppCode() != null) {
            queryParams.add(new QueryParam("appCode", QueryOperator.LIKE, "%" + queryManagedAppOrder.getAppCode() + "%"));
        }

        return queryParams;
    }

    // 构建分页
    private Pageable buildPageable(QueryManagedAppOrder queryManagedAppOrder) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return new PageRequest(queryManagedAppOrder.getPageNo() - 1, queryManagedAppOrder.getPageSize(), sort);
    }
}
