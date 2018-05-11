/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:52 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.FindAppPropertyKeysOrder;
import org.antframework.configcenter.facade.order.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.FindAppPropertyKeysResult;
import org.antframework.configcenter.facade.result.QueryPropertyKeyResult;

/**
 * 属性key服务
 */
public interface PropertyKeyService {
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
    FindAppPropertyKeysResult findAppPropertyKeys(FindAppPropertyKeysOrder order);

    /**
     * 查询属性key
     */
    QueryPropertyKeyResult queryPropertyKey(QueryPropertyKeyOrder order);
}
