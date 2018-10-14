/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:07 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.FindAppTreeResult;
import org.antframework.configcenter.facade.result.FindInheritedAppsResult;
import org.antframework.configcenter.facade.result.QueryAppsResult;

/**
 * 应用服务
 */
public interface AppService {
    /**
     * 添加或修改应用
     */
    EmptyResult addOrModifyApp(AddOrModifyAppOrder order);

    /**
     * 删除应用
     */
    EmptyResult deleteApp(DeleteAppOrder order);

    /**
     * 查找应用
     */
    FindAppResult findApp(FindAppOrder order);

    /**
     * 查找应用继承的所有应用
     */
    FindInheritedAppsResult findInheritedApps(FindInheritedAppsOrder order);

    /**
     * 查找应用树
     */
    FindAppTreeResult findAppTree(FindAppTreeOrder order);

    /**
     * 查询应用
     */
    QueryAppsResult queryApps(QueryAppsOrder order);
}
