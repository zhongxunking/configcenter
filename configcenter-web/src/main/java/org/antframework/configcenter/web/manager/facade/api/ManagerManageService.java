/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 10:39 创建
 */
package org.antframework.configcenter.web.manager.facade.api;

import org.antframework.configcenter.web.manager.facade.order.*;
import org.antframework.configcenter.web.manager.facade.result.*;

/**
 * 管理员管理服务
 */
public interface ManagerManageService {

    /**
     * 添加管理员
     */
    AddManagerResult addManager(AddManagerOrder order);

    /**
     * 删除管理员
     */
    DeleteManagerResult deleteManager(DeleteManagerOrder order);

    /**
     * 修改管理员密码
     */
    ModifyManagerPasswordResult modifyManagerPassword(ModifyManagerPasswordOrder order);

    /**
     * 修改管理员类型
     */
    ModifyManagerTypeResult modifyManagerType(ModifyManagerTypeOrder order);

    /**
     * 修改管理员名称
     */
    ModifyManagerNameResult modifyManagerName(ModifyManagerNameOrder order);

    /**
     * 查询管理员
     */
    QueryManagerResult queryManager(QueryManagerOrder order);

    /**
     * 管理员登录
     */
    ManagerLoginResult managerLogin(ManagerLoginOrder order);
}
