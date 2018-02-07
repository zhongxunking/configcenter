/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:08 创建
 */
package org.antframework.configcenter.facade.api.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.manage.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.FindAppProfilePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.manage.FindAppProfilePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;

/**
 * 属性value服务
 */
public interface PropertyValueService {
    /**
     * 设置多个属性value
     */
    EmptyResult setPropertyValues(SetPropertyValuesOrder order);

    /**
     * 删除属性value
     */
    EmptyResult deletePropertyValue(DeletePropertyValueOrder order);

    /**
     * 查找应用在指定环境的所有属性value
     */
    FindAppProfilePropertyValueResult findAppProfilePropertyValue(FindAppProfilePropertyValueOrder order);

    /**
     * 查询属性value
     */
    QueryPropertyValueResult queryPropertyValue(QueryPropertyValueOrder order);
}
