/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:07 创建
 */
package org.antframework.configcenter.facade.api.manage;


import org.antframework.configcenter.facade.order.manage.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.order.manage.DeleteAppOrder;
import org.antframework.configcenter.facade.order.manage.QueryAppOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyAppResult;
import org.antframework.configcenter.facade.result.manage.DeleteAppResult;
import org.antframework.configcenter.facade.result.manage.QueryAppResult;

/**
 *
 */
public interface AppManageService {

    AddOrModifyAppResult addOrModifyApp(AddOrModifyAppOrder order);

    DeleteAppResult deleteApp(DeleteAppOrder order);

    QueryAppResult queryApp(QueryAppOrder order);
}
