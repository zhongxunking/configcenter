/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-02 22:30 创建
 */
package org.antframework.configcenter.spring;

import org.antframework.configcenter.client.Config;
import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.spring.boot.ConfigcenterProperties;

/**
 * 配置上下文操作类
 */
public final class ConfigContexts {
    // 配置上下文
    private static final ConfigContext CONFIG_CONTEXT = new ConfigContext(
            ConfigcenterProperties.INSTANCE.getRequiredAppId(),
            ConfigcenterProperties.INSTANCE.getRequiredProfileId(),
            ConfigcenterProperties.INSTANCE.getTarget(),
            ConfigcenterProperties.INSTANCE.getServerUrl(),
            ConfigcenterProperties.INSTANCE.computeHome(),
            ConfigcenterProperties.INSTANCE.getManagerId(),
            ConfigcenterProperties.INSTANCE.getSecretKey());

    /**
     * 获取配置上下文
     */
    public static ConfigContext getContext() {
        return CONFIG_CONTEXT;
    }

    /**
     * 获取配置
     *
     * @param appId 被查询配置的应用id
     * @return 配置
     */
    public static Config getConfig(String appId) {
        return CONFIG_CONTEXT.getConfig(appId);
    }
}
