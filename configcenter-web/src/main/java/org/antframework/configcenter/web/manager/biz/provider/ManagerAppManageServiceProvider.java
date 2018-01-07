/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:34 创建
 */
package org.antframework.configcenter.web.manager.biz.provider;

import org.antframework.boot.bekit.CommonQueryConstant;
import org.antframework.boot.bekit.CommonQueryResult;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.facade.api.ManagerAppManageService;
import org.antframework.configcenter.web.manager.facade.order.*;
import org.antframework.configcenter.web.manager.facade.result.FindManagerAppResult;
import org.antframework.configcenter.web.manager.facade.result.QueryManagedAppResult;
import org.antframework.configcenter.web.manager.facade.result.QueryManagerAppResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员关联应用管理服务提供者
 */
@Service
public class ManagerAppManageServiceProvider implements ManagerAppManageService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public EmptyResult addManagerApp(AddManagerAppOrder order) {
        return serviceEngine.execute("addManagerAppService", order);
    }

    @Override
    public EmptyResult deleteManagerApp(DeleteManagerAppOrder order) {
        return serviceEngine.execute("deleteManagerAppService", order);
    }

    @Override
    public EmptyResult deleteManagerAppByApp(DeleteManagerAppByAppOrder order) {
        return serviceEngine.execute("deleteManagerAppByAppService", order);
    }

    @Override
    public FindManagerAppResult findManagerApp(FindManagerAppOrder order) {
        return serviceEngine.execute("findManagerAppService", order);
    }

    @Override
    public QueryManagedAppResult queryManagedApp(QueryManagedAppOrder order) {
        return serviceEngine.execute("queryManagedAppService", order);
    }

    @Override
    public QueryManagerAppResult queryManagerApp(QueryManagerAppOrder order) {
        CommonQueryResult result = serviceEngine.execute(CommonQueryConstant.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachment(ManagerAppDao.class));
        return result.convertTo(QueryManagerAppResult.class);
    }
}
