/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:52 创建
 */
package org.antframework.configcenter.facade.api.manage;

import org.antframework.configcenter.facade.order.manage.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyPropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.DeletePropertyKeyResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyKeyResult;

/**
 * 属性key管理服务
 */
public interface PropertyKeyManageService {
    /**
     * 添加或修改属性key
     */
    AddOrModifyPropertyKeyResult addOrModifyPropertyKey(AddOrModifyPropertyKeyOrder order);

    /**
     * 删除属性key
     */
    DeletePropertyKeyResult deletePropertyKey(DeletePropertyKeyOrder order);

    /**
     * 查询属性key
     */
    QueryPropertyKeyResult queryPropertyKey(QueryPropertyKeyOrder order);
}
