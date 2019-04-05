/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:42 创建
 */
package org.antframework.configcenter.biz.provider;

import lombok.AllArgsConstructor;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindAppSelfConfigOrder;
import org.antframework.configcenter.facade.order.FindConfigOrder;
import org.antframework.configcenter.facade.result.FindAppSelfConfigResult;
import org.antframework.configcenter.facade.result.FindConfigResult;
import org.bekit.service.ServiceEngine;
import org.springframework.stereotype.Service;

/**
 * 配置服务提供者
 */
@Service
@AllArgsConstructor
public class ConfigServiceProvider implements ConfigService {
    // 服务引擎
    private final ServiceEngine serviceEngine;

    @Override
    public FindConfigResult findConfig(FindConfigOrder order) {
        return serviceEngine.execute("findConfigService", order);
    }

    @Override
    public FindAppSelfConfigResult findAppSelfConfig(FindAppSelfConfigOrder order) {
        return serviceEngine.execute("findAppSelfConfigService", order);
    }
}
