/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:42 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.order.QueryAppProfilePropertyOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.QueryAppProfilePropertyResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class ConfigServiceProvider implements ConfigService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public FindAppResult findApp(FindAppOrder order) {
        return serviceEngine.execute("findAppService", order);
    }

    @Override
    public QueryAppProfilePropertyResult queryAppProfileProperty(QueryAppProfilePropertyOrder order) {
        return serviceEngine.execute("queryAppProfilePropertyService", order);
    }
}
