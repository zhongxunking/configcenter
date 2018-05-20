/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:42 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindAppSelfPropertiesOrder;
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppSelfPropertiesResult;
import org.antframework.configcenter.facade.result.FindPropertiesResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配置服务提供者
 */
@Service
public class ConfigServiceProvider implements ConfigService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public FindPropertiesResult findProperties(FindPropertiesOrder order) {
        return serviceEngine.execute("findPropertiesService", order);
    }

    @Override
    public FindAppSelfPropertiesResult findAppSelfProperties(FindAppSelfPropertiesOrder order) {
        return serviceEngine.execute("findAppSelfPropertiesService", order);
    }
}
