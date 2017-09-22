/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-22 18:20 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.facade.order.DeleteManagerAppByAppOrder;
import org.antframework.configcenter.web.manager.facade.result.DeleteManagerAppByAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除所有管理员与指定应用的关联服务
 */
@Service(enableTx = true)
public class DeleteManagerAppByAppService {
    @Autowired
    private ManagerAppDao managerAppDao;

    @ServiceExecute
    public void execute(ServiceContext<DeleteManagerAppByAppOrder, DeleteManagerAppByAppResult> context) {
        DeleteManagerAppByAppOrder order = context.getOrder();

        managerAppDao.deleteByAppCode(order.getAppCode());
    }
}
