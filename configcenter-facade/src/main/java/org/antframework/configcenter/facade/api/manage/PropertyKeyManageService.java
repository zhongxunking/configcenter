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
 *
 */
public interface PropertyKeyManageService {

    AddOrModifyPropertyKeyResult addOrModifyPropertyKey(AddOrModifyPropertyKeyOrder order);

    DeletePropertyKeyResult deletePropertyKey(DeletePropertyKeyOrder order);

    QueryPropertyKeyResult queryPropertyKey(QueryPropertyKeyOrder order);
}
