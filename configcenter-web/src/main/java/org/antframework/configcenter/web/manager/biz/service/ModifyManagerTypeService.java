/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:09 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.facade.order.ModifyManagerTypeOrder;
import org.antframework.configcenter.web.manager.facade.result.ModifyManagerTypeResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 修改管理员类型服务
 */
@Service(enableTx = true)
public class ModifyManagerTypeService {
    @Autowired
    private ManagerDao managerDao;

    @ServiceExecute
    public void execute(ServiceContext<ModifyManagerTypeOrder, ModifyManagerTypeResult> context) {
        ModifyManagerTypeOrder order = context.getOrder();

        Manager manager = managerDao.findLockByManagerCode(order.getManagerCode());
        if (manager == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("管理员[%s]不存在", order.getManagerCode()));
        }
        manager.setType(order.getNewType());
        managerDao.save(manager);
    }
}
