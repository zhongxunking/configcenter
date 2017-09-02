/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:00 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.FacadeUtils.SpringDataPageExtractor;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.order.manage.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.QueryPropertyKeyResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询属性key服务
 */
@Service
public class QueryPropertyKeyService {
    @Autowired
    private PropertyKeyDao propertyKeyDao;

    @ServiceExecute
    public void execute(ServiceContext<QueryPropertyKeyOrder, QueryPropertyKeyResult> context) {
        QueryPropertyKeyOrder order = context.getOrder();

        Page<PropertyKey> page = propertyKeyDao.query(buildSearchParams(order), new PageRequest(order.getPageNo() - 1, order.getPageSize()));
        FacadeUtils.setQueryResult(context.getResult(), new SpringDataPageExtractor<>(page));
    }

    // 构建查询条件
    private Map<String, Object> buildSearchParams(QueryPropertyKeyOrder queryPropertyKeyOrder) {
        Map<String, Object> searchParams = new HashMap<>();
        if (queryPropertyKeyOrder.getAppCode() != null) {
            searchParams.put("LIKE_appCode", "%" + queryPropertyKeyOrder.getAppCode() + "%");
        }
        if (queryPropertyKeyOrder.getKey() != null) {
            searchParams.put("LIKE_key", "%" + queryPropertyKeyOrder.getKey() + "%");
        }
        if (queryPropertyKeyOrder.getCommon() != null) {
            searchParams.put("EQ_common", queryPropertyKeyOrder.getCommon());
        }
        return searchParams;
    }
}
