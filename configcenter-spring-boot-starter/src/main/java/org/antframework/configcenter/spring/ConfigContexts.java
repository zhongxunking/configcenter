/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-02 22:30 创建
 */
package org.antframework.configcenter.spring;

import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.spring.boot.ConfigcenterProperties;
import org.antframework.configcenter.spring.context.Contexts;

/**
 * 配置上下文操作类
 */
public final class ConfigContexts {
    // 配置上下文
    private static final ConfigContext CONFIG_CONTEXT;

    static {
        // 创建配置上下文
        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setServerUrl(ConfigcenterProperties.INSTANCE.getServerUrl());
        initParams.setMainAppId(Contexts.getAppId());
        initParams.setProfileId(Contexts.getProfile());
        initParams.setCacheDir(ConfigcenterProperties.INSTANCE.getCacheDir());

        CONFIG_CONTEXT = new ConfigContext(initParams);
    }

    /**
     * 获取配置上下文
     */
    public static ConfigContext getContext() {
        return CONFIG_CONTEXT;
    }
}
