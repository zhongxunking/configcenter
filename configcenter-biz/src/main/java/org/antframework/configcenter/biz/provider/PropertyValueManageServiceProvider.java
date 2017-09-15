/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:03 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.configcenter.facade.api.manage.PropertyValueManageService;
import org.antframework.configcenter.facade.order.manage.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.FindAppProfilePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.SetPropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.DeletePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.FindAppProfilePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;
import org.antframework.configcenter.facade.result.manage.SetPropertyValueResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 属性value管理服务提供者
 */
@Service
public class PropertyValueManageServiceProvider implements PropertyValueManageService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public SetPropertyValueResult setPropertyValue(SetPropertyValueOrder order) {
        return serviceEngine.execute("setPropertyValueService", order);
    }

    @Override
    public DeletePropertyValueResult deletePropertyValue(DeletePropertyValueOrder order) {
        return serviceEngine.execute("deletePropertyValueService", order);
    }

    @Override
    public FindAppProfilePropertyValueResult findAppProfilePropertyValue(FindAppProfilePropertyValueOrder order) {
        return serviceEngine.execute("findAppProfilePropertyValueService", order);
    }

    @Override
    public QueryPropertyValueResult queryPropertyValue(QueryPropertyValueOrder order) {
        return serviceEngine.execute("queryPropertyValueService", order);
    }
}
