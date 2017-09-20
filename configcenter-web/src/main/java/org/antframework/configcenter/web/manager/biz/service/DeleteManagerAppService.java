/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:44 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.dal.entity.ManagerApp;
import org.antframework.configcenter.web.manager.facade.order.DeleteManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.result.DeleteManagerAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除管理员与应用关联服务
 */
@Service(enableTx = true)
public class DeleteManagerAppService {
    @Autowired
    private ManagerAppDao managerAppDao;

    @ServiceExecute
    public void execute(ServiceContext<DeleteManagerAppOrder, DeleteManagerAppResult> context) {
        DeleteManagerAppOrder order = context.getOrder();

        ManagerApp managerApp = managerAppDao.findLockByManagerCodeAndAppCode(order.getManagerCode(), order.getAppCode());
        if (managerApp != null) {
            managerAppDao.delete(managerApp);
        }
    }
}
