/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 10:39 创建
 */
package org.antframework.configcenter.web.manager.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.web.manager.facade.order.*;
import org.antframework.configcenter.web.manager.facade.result.ManagerLoginResult;
import org.antframework.configcenter.web.manager.facade.result.QueryManagerResult;

/**
 * 管理员管理服务
 */
public interface ManagerManageService {

    /**
     * 添加管理员
     */
    EmptyResult addManager(AddManagerOrder order);

    /**
     * 删除管理员
     */
    EmptyResult deleteManager(DeleteManagerOrder order);

    /**
     * 修改管理员密码
     */
    EmptyResult modifyManagerPassword(ModifyManagerPasswordOrder order);

    /**
     * 修改管理员类型
     */
    EmptyResult modifyManagerType(ModifyManagerTypeOrder order);

    /**
     * 修改管理员名称
     */
    EmptyResult modifyManagerName(ModifyManagerNameOrder order);

    /**
     * 查询管理员
     */
    QueryManagerResult queryManager(QueryManagerOrder order);

    /**
     * 管理员登录
     */
    ManagerLoginResult managerLogin(ManagerLoginOrder order);
}
