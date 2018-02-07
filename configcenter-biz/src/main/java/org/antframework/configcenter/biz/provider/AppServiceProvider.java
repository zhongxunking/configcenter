/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:59 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.boot.bekit.CommonQueryConstant;
import org.antframework.boot.bekit.CommonQueryResult;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.facade.api.manage.AppService;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.order.manage.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.order.manage.DeleteAppOrder;
import org.antframework.configcenter.facade.order.manage.QueryAppOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.manage.QueryAppResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 应用服务提供者
 */
@Service
public class AppServiceProvider implements AppService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public EmptyResult addOrModifyApp(AddOrModifyAppOrder order) {
        return serviceEngine.execute("addOrModifyAppService", order);
    }

    @Override
    public EmptyResult deleteApp(DeleteAppOrder order) {
        return serviceEngine.execute("deleteAppService", order);
    }

    @Override
    public FindAppResult findApp(FindAppOrder order) {
        return serviceEngine.execute("findAppService", order);
    }

    @Override
    public QueryAppResult queryApp(QueryAppOrder order) {
        CommonQueryResult result = serviceEngine.execute(CommonQueryConstant.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachment(AppDao.class));
        return result.convertTo(QueryAppResult.class);
    }
}
