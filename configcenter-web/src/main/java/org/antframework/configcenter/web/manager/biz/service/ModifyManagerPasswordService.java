/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:02 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.facade.order.ModifyManagerPasswordOrder;
import org.antframework.configcenter.web.manager.facade.result.ModifyManagerPasswordResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 修改管理员密码服务
 */
@Service(enableTx = true)
public class ModifyManagerPasswordService {
    @Autowired
    private ManagerDao managerDao;

    @ServiceExecute
    public void execute(ServiceContext<ModifyManagerPasswordOrder, ModifyManagerPasswordResult> context) {
        ModifyManagerPasswordOrder order = context.getOrder();

        Manager manager = managerDao.findLockByCode(order.getCode());
        if (manager == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("管理员[%s]不存在", order.getCode()));
        }
        manager.setPassword(order.getNewPassword());
        managerDao.save(manager);
    }
}
