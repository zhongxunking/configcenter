/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:07 创建
 */
package org.antframework.configcenter.facade.api;


import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.order.DeleteAppOrder;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.order.QueryAppOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.QueryAppResult;

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
     * 查询应用
     */
    QueryAppResult queryApp(QueryAppOrder order);
}
