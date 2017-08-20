/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:08 创建
 */
package org.antframework.configcenter.facade.api.manage;

import org.antframework.configcenter.facade.order.manage.SetPropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.SetPropertyValueResult;
import org.antframework.configcenter.facade.result.manage.DeletePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;

/**
 *
 */
public interface PropertyValueManageService {

    SetPropertyValueResult setPropertyValue(SetPropertyValueOrder order);

    DeletePropertyValueResult deletePropertyValue(DeletePropertyValueOrder order);

    QueryPropertyValueResult queryPropertyValue(QueryPropertyValueOrder order);
}
