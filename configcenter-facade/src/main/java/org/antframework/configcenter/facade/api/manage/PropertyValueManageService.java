/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:08 创建
 */
package org.antframework.configcenter.facade.api.manage;

import org.antframework.configcenter.facade.order.manage.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.SetPropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.DeletePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;
import org.antframework.configcenter.facade.result.manage.SetPropertyValueResult;

/**
 * 属性value管理服务
 */
public interface PropertyValueManageService {
    /**
     * 设置属性value
     */
    SetPropertyValueResult setPropertyValue(SetPropertyValueOrder order);

    /**
     * 删除属性value
     */
    DeletePropertyValueResult deletePropertyValue(DeletePropertyValueOrder order);

    /**
     * 查询属性value
     */
    QueryPropertyValueResult queryPropertyValue(QueryPropertyValueOrder order);
}
