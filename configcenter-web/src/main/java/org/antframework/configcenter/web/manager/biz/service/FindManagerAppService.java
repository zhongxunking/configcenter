/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 18:59 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.dal.entity.ManagerApp;
import org.antframework.configcenter.web.manager.facade.info.ManagerAppInfo;
import org.antframework.configcenter.web.manager.facade.order.FindManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.result.FindManagerAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 查找管理员与应用关联服务
 */
@Service
public class FindManagerAppService {
    @Autowired
    private ManagerAppDao managerAppDao;

    @ServiceExecute
    public void execute(ServiceContext<FindManagerAppOrder, FindManagerAppResult> context) {
        FindManagerAppOrder order = context.getOrder();
        FindManagerAppResult result = context.getResult();

        ManagerApp managerApp = managerAppDao.findByManagerCodeAndAppCode(order.getManagerCode(), order.getAppCode());
        if (managerApp != null) {
            result.setInfo(buildInfo(managerApp));
        }
    }

    // 构建info
    private ManagerAppInfo buildInfo(ManagerApp managerApp) {
        ManagerAppInfo info = new ManagerAppInfo();
        BeanUtils.copyProperties(managerApp, info);
        return info;
    }
}
