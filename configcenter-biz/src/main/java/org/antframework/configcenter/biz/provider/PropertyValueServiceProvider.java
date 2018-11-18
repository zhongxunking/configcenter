/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:03 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配置value服务提供者
 */
@Service
public class PropertyValueServiceProvider implements PropertyValueService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public EmptyResult setPropertyValues(SetPropertyValuesOrder order) {
        return serviceEngine.execute("setPropertyValuesService", order);
    }

    @Override
    public FindAppProfilePropertyValuesResult findAppProfilePropertyValues(FindAppProfilePropertyValuesOrder order) {
        return serviceEngine.execute("findAppProfilePropertyValuesService", order);
    }
}
