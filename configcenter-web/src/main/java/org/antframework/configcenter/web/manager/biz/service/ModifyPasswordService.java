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
import org.antframework.configcenter.web.manager.facade.order.ModifyPasswordOrder;
import org.antframework.configcenter.web.manager.facade.result.ModifyPasswordResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 修改密码服务
 */
@Service
public class ModifyPasswordService {
    @Autowired
    private ManagerDao managerDao;

    @ServiceExecute
    public void execute(ServiceContext<ModifyPasswordOrder, ModifyPasswordResult> context) {
        ModifyPasswordOrder order = context.getOrder();

        Manager manager = managerDao.findLockByUsername(order.getUsername());
        if (manager == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("用户[%s]不存在", order.getUsername()));
        }
        manager.setPassword(order.getNewPassword());
        managerDao.save(manager);
    }
}
