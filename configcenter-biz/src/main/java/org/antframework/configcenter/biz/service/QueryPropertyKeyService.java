/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:00 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.manage.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.QueryPropertyKeyResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class QueryPropertyKeyService {
    @Autowired
    private PropertyKeyDao propertyKeyDao;

    @ServiceExecute
    public void execute(ServiceContext<QueryPropertyKeyOrder, QueryPropertyKeyResult> serviceContext) {
        QueryPropertyKeyOrder order = serviceContext.getOrder();

        Page<PropertyKey> page = propertyKeyDao.query(buildSearchParams(order), new PageRequest(order.getPageNo() - 1, order.getPageSize()));
        setResult(serviceContext.getResult(), page);
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
        return searchParams;
    }

    // 设置result
    private void setResult(QueryPropertyKeyResult result, Page<PropertyKey> page) {
        result.setTotalCount(page.getTotalElements());

        for (PropertyKey propertyKey : page.getContent()) {
            PropertyKeyInfo info = new PropertyKeyInfo();
            BeanUtils.copyProperties(propertyKey, info);

            result.addInfo(info);
        }
    }
}
