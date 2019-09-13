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
import org.antframework.configcenter.facade.order.FindPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindPropertyKeysResult;

/**
 * 配置key服务
 */
public interface PropertyKeyService {
    /**
     * 添加或修改配置key
     */
    EmptyResult addOrModifyPropertyKey(AddOrModifyPropertyKeyOrder order);

    /**
     * 删除配置key
     */
    EmptyResult deletePropertyKey(DeletePropertyKeyOrder order);

    /**
     * 查找配置key集
     */
    FindPropertyKeysResult findPropertyKeys(FindPropertyKeysOrder order);
}
