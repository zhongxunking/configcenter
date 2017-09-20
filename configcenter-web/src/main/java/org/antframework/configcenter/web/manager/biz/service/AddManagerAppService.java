/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:35 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.dal.entity.ManagerApp;
import org.antframework.configcenter.web.manager.facade.order.AddManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.result.AddManagerAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 添加管理员与应用关联服务
 */
@Service(enableTx = true)
public class AddManagerAppService {
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private ManagerAppDao managerAppDao;

    @ServiceExecute
    public void execute(ServiceContext<AddManagerAppOrder, AddManagerAppResult> context) {
        AddManagerAppOrder order = context.getOrder();

        Manager manager = managerDao.findLockByUsername(order.getUsername());
        if (manager == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("管理员[%s]不存在", order.getUsername()));
        }
        ManagerApp managerApp = managerAppDao.findLockByUsernameAndAppCode(order.getUsername(), order.getAppCode());
        if (managerApp == null) {
            managerApp = buildManagerApp(order);
            managerAppDao.save(managerApp);
        }
    }

    // 构建管理员与应用关联
    private ManagerApp buildManagerApp(AddManagerAppOrder addManagerAppOrder) {
        ManagerApp managerApp = new ManagerApp();
        BeanUtils.copyProperties(addManagerAppOrder, managerApp);
        return managerApp;
    }
}
