/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:08 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;

/**
 * 配置value服务
 */
public interface PropertyValueService {
    /**
     * 新增或修改配置value
     */
    EmptyResult addOrModifyPropertyValue(AddOrModifyPropertyValueOrder order);

    /**
     * 删除配置value
     */
    EmptyResult deletePropertyValue(DeletePropertyValueOrder order);

    /**
     * 查找应用在指定环境的所有配置value
     */
    FindAppProfilePropertyValuesResult findAppProfilePropertyValues(FindAppProfilePropertyValuesOrder order);
}
