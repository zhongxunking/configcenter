/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:27 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.manage.QueryAppOrder;
import org.antframework.configcenter.facade.result.manage.QueryAppResult;
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
 * 查询应用服务
 */
@Service
public class QueryAppService {
    @Autowired
    private AppDao appDao;

    @ServiceExecute
    public void execute(ServiceContext<QueryAppOrder, QueryAppResult> serviceContext) {
        QueryAppOrder order = serviceContext.getOrder();

        Page<App> page = appDao.query(buildSearchParams(order), new PageRequest(order.getPageNo() - 1, order.getPageSize()));
        setResult(serviceContext.getResult(), page);
    }

    // 构建查询条件
    private Map<String, Object> buildSearchParams(QueryAppOrder queryAppOrder) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("LIKE_appCode", queryAppOrder.getAppCode());
        return searchParams;
    }

    // 设置结果
    private void setResult(QueryAppResult result, Page<App> page) {
        result.setTotalCount(page.getTotalElements());

        for (App app : page.getContent()) {
            AppInfo info = new AppInfo();
            BeanUtils.copyProperties(app, info);

            result.addInfo(info);
        }
    }
}
