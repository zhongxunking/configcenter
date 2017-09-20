/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 10:45 创建
 */
package org.antframework.configcenter.web.manager.biz.provider;

import org.antframework.configcenter.web.manager.facade.api.ManagerManageService;
import org.antframework.configcenter.web.manager.facade.order.*;
import org.antframework.configcenter.web.manager.facade.result.*;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员管理服务提供者
 */
@Service
public class ManagerManageServiceProvider implements ManagerManageService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public AddManagerResult addManager(AddManagerOrder order) {
        return serviceEngine.execute("addManagerService", order);
    }

    @Override
    public DeleteManagerResult deleteManager(DeleteManagerOrder order) {
        return serviceEngine.execute("deleteManagerService", order);
    }

    @Override
    public ModifyPasswordResult modifyPassword(ModifyPasswordOrder order) {
        return serviceEngine.execute("modifyPasswordService", order);
    }

    @Override
    public ModifyManagerTypeResult modifyManagerType(ModifyManagerTypeOrder order) {
        return serviceEngine.execute("modifyManagerTypeService", order);
    }

    @Override
    public QueryManagerResult queryManager(QueryManagerOrder order) {
        return serviceEngine.execute("queryManagerService", order);
    }

    @Override
    public ManagerLoginResult managerLogin(ManagerLoginOrder order) {
        return serviceEngine.execute("managerLoginService", order);
    }
}
