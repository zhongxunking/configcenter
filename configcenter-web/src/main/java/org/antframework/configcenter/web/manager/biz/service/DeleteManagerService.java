/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 10:55 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.facade.order.DeleteManagerOrder;
import org.antframework.configcenter.web.manager.facade.result.DeleteManagerResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除管理员服务
 */
@Service(enableTx = true)
public class DeleteManagerService {
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private ManagerAppDao managerAppDao;

    @ServiceExecute
    public void execute(ServiceContext<DeleteManagerOrder, DeleteManagerResult> context) {
        DeleteManagerOrder order = context.getOrder();

        Manager manager = managerDao.findLockByManagerCode(order.getManagerCode());
        if (manager == null) {
            return;
        }
        if (managerAppDao.existsByManagerCode(order.getManagerCode())) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("管理员[%s]还管理着应用，不能删除", order.getManagerCode()));
        }
        managerDao.delete(manager);
    }
}
