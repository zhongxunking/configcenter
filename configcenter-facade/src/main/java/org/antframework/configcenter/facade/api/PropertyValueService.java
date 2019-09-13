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
import org.antframework.configcenter.facade.order.FindPropertyValuesOrder;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindPropertyValuesResult;

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
     * 回滚配置value
     */
    EmptyResult revertPropertyValues(RevertPropertyValuesOrder order);

    /**
     * 查找配置value集
     */
    FindPropertyValuesResult findPropertyValues(FindPropertyValuesOrder order);
}
