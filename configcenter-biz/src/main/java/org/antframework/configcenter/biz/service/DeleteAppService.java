/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:22 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.order.manage.DeleteAppOrder;
import org.antframework.configcenter.facade.result.manage.DeleteAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service(enableTx = true)
public class DeleteAppService {

    @Autowired
    private AppDao appDao;

    @ServiceExecute
    public void serviceExecute(ServiceContext<DeleteAppOrder, DeleteAppResult> serviceContext) {
        DeleteAppOrder order = serviceContext.getOrder();

        App app = appDao.findLockByAppCode(order.getAppCode());
        if (app != null) {
            appDao.deleteByAppCode(order.getAppCode());
        }
    }
}
