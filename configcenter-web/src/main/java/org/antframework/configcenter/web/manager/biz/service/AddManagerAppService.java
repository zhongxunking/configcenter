/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:35 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
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
@Service
public class AddManagerAppService {
    @Autowired
    private ManagerAppDao managerAppDao;

    @ServiceExecute
    public void execute(ServiceContext<AddManagerAppOrder, AddManagerAppResult> context) {
        AddManagerAppOrder order = context.getOrder();

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
