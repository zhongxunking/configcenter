/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:29 创建
 */
package org.antframework.configcenter.web.manager.facade.api;

import org.antframework.configcenter.web.manager.facade.order.*;
import org.antframework.configcenter.web.manager.facade.result.*;

/**
 * 管理员关联应用管理服务
 */
public interface ManagerAppManageService {

    /**
     * 添加管理员与应用关联
     */
    AddManagerAppResult addManagerApp(AddManagerAppOrder order);

    /**
     * 删除管理员与应用关联
     */
    DeleteManagerAppResult deleteManagerApp(DeleteManagerAppOrder order);

    /**
     * 查找管理员与应用关联
     */
    FindManagerAppResult findManagerApp(FindManagerAppOrder order);

    /**
     * 查询被管理员管理的应用
     */
    QueryManagedAppResult queryManagedApp(QueryManagedAppOrder order);

    /**
     * 查询管理员和应用关联
     */
    QueryManagerAppResult queryManagerApp(QueryManagerAppOrder order);
}
