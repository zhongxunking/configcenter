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

import java.util.HashMap;
import java.util.Map;

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

        Page<ManagerApp> page = managerAppDao.query(buildSearchParams(order), new PageRequest(order.getPageNo() - 1, order.getPageSize()));
        FacadeUtils.setQueryResult(context.getResult(), new FacadeUtils.SpringDataPageExtractor<>(page));
    }

    // 构建searchParams
    public Map<String, Object> buildSearchParams(QueryManagedAppOrder queryManagedAppOrder) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_managerCode", queryManagedAppOrder.getManagerCode());
        if (queryManagedAppOrder.getAppCode() != null) {
            searchParams.put("LIKE_appCode", "%" + queryManagedAppOrder.getAppCode() + "%");
        }
        return searchParams;
    }
}
