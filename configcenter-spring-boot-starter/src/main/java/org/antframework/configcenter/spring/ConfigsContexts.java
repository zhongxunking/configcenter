/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-02 22:30 创建
 */
package org.antframework.configcenter.spring;

import org.antframework.configcenter.client.Config;
import org.antframework.configcenter.client.ConfigsContext;
import org.antframework.configcenter.spring.boot.ConfigcenterProperties;
import org.antframework.configcenter.spring.context.Contexts;

/**
 * 配置上下文操作类
 */
public final class ConfigsContexts {
    // 配置上下文
    private static final ConfigsContext CONFIGS_CONTEXT = new ConfigsContext(
            Contexts.getAppId(),
            Contexts.getProfile(),
            ConfigcenterProperties.INSTANCE.getServerUrl(),
            ConfigcenterProperties.INSTANCE.getCacheDirPath());

    /**
     * 获取配置上下文
     */
    public static ConfigsContext getContext() {
        return CONFIGS_CONTEXT;
    }

    /**
     * 获取配置
     *
     * @param appId 被查询配置的应用id
     * @return 配置
     */
    public static Config getConfig(String appId) {
        return CONFIGS_CONTEXT.getConfig(appId);
    }
}
