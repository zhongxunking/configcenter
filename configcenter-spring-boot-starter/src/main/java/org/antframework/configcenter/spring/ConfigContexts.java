/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-02 22:30 创建
 */
package org.antframework.configcenter.spring;

import org.antframework.common.util.other.Cache;
import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.spring.boot.ConfigcenterProperties;
import org.antframework.configcenter.spring.context.Contexts;

import java.io.File;
import java.util.Set;

/**
 * 配置上下文操作类
 */
public final class ConfigContexts {
    // 配置上下文缓存
    private static final Cache<String, ConfigContext> CACHE = new Cache<>(new Cache.Supplier<String, ConfigContext>() {
        @Override
        public ConfigContext get(String key) {
            return buildConfigContext(key);
        }
    });

    /**
     * 获取配置上下文
     *
     * @param appId 被查询配置的应用id
     */
    public static ConfigContext get(String appId) {
        return CACHE.get(appId);
    }

    /**
     * 获取已缓存的应用id
     */
    public static Set<String> getAppIds() {
        return CACHE.getAllKeys();
    }

    // 构建配置上下文
    private static ConfigContext buildConfigContext(String queriedAppId) {
        ConfigcenterProperties properties = Contexts.buildProperties(ConfigcenterProperties.class);

        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setMainAppId(Contexts.getAppId());
        initParams.setQueriedAppId(queriedAppId);
        initParams.setProfileId(Contexts.getProfile());
        initParams.setServerUrl(properties.getServerUrl());
        initParams.setCacheFilePath(properties.getCacheDirPath() + File.separator + String.format("configcenter-%s-%s.properties", queriedAppId, Contexts.getProfile()));

        return new ConfigContext(initParams);
    }
}
