/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:17 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.facade.order.QueryManagerOrder;
import org.antframework.configcenter.web.manager.facade.result.QueryManagerResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询管理员服务
 */
@Service
public class QueryManagerService {
    @Autowired
    private ManagerDao managerDao;

    @ServiceExecute
    public void execute(ServiceContext<QueryManagerOrder, QueryManagerResult> context) {
        QueryManagerOrder order = context.getOrder();

        Page<Manager> page = managerDao.query(buildSearchParams(order), buildPageable(order));
        FacadeUtils.setQueryResult(context.getResult(), new FacadeUtils.SpringDataPageExtractor<>(page));
    }

    private Map<String, Object> buildSearchParams(QueryManagerOrder queryManagerOrder) {
        Map<String, Object> searchParams = new HashMap<>();
        if (queryManagerOrder.getManagerCode() != null) {
            searchParams.put("LIKE_managerCode", "%" + queryManagerOrder.getManagerCode() + "%");
        }
        if (queryManagerOrder.getName() != null) {
            searchParams.put("LIKE_name", "%" + queryManagerOrder.getName() + "%");
        }
        if (queryManagerOrder.getType() != null) {
            searchParams.put("EQ_type", queryManagerOrder.getType());
        }
        return searchParams;
    }

    // 构建分页
    private Pageable buildPageable(QueryManagerOrder queryManagerOrder) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return new PageRequest(queryManagerOrder.getPageNo() - 1, queryManagerOrder.getPageSize(), sort);
    }
}
