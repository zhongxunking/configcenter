/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:38 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.FacadeUtils.SpringDataPageExtractor;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询属性value服务
 */
@Service
public class QueryPropertyValueService {
    @Autowired
    private PropertyValueDao propertyValueDao;

    @ServiceExecute
    public void execute(ServiceContext<QueryPropertyValueOrder, QueryPropertyValueResult> context) {
        QueryPropertyValueOrder order = context.getOrder();

        Page<PropertyValue> page = propertyValueDao.query(buildSearchParams(order), new PageRequest(order.getPageNo() - 1, order.getPageSize()));
        FacadeUtils.setQueryResult(context.getResult(), new SpringDataPageExtractor<>(page));
    }

    // 构建查询条件
    private Map<String, Object> buildSearchParams(QueryPropertyValueOrder queryPropertyValueOrder) {
        Map<String, Object> searchParams = new HashMap<>();
        if (queryPropertyValueOrder.getAppCode() != null) {
            searchParams.put("LIKE_appCode", "%" + queryPropertyValueOrder.getAppCode() + "%");
        }
        if (queryPropertyValueOrder.getKey() != null) {
            searchParams.put("LIKE_key", "%" + queryPropertyValueOrder.getKey() + "%");
        }
        if (queryPropertyValueOrder.getProfileCode() != null) {
            searchParams.put("LIKE_profileCode", "%" + queryPropertyValueOrder.getProfileCode() + "%");
        }
        return searchParams;
    }
}
