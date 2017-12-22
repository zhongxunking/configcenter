/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:52 创建
 */
package org.antframework.configcenter.facade.api.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.manage.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.FindAppPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.FindAppPropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyKeyResult;

/**
 * 属性key管理服务
 */
public interface PropertyKeyManageService {
    /**
     * 添加或修改属性key
     */
    EmptyResult addOrModifyPropertyKey(AddOrModifyPropertyKeyOrder order);

    /**
     * 删除属性key
     */
    EmptyResult deletePropertyKey(DeletePropertyKeyOrder order);

    /**
     * 查找应用所有的属性key
     */
    FindAppPropertyKeyResult findAppPropertyKey(FindAppPropertyKeyOrder order);

    /**
     * 查询属性key
     */
    QueryPropertyKeyResult queryPropertyKey(QueryPropertyKeyOrder order);
}
