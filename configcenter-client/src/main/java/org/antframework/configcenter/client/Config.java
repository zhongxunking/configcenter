/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-19 00:13 创建
 */
package org.antframework.configcenter.client;

import org.antframework.common.util.file.MapFile;
import org.antframework.configcenter.client.core.ConfigProperties;
import org.antframework.configcenter.client.core.ConfigurableConfigProperties;
import org.antframework.configcenter.client.core.DefaultConfigProperties;
import org.antframework.configcenter.client.support.ConfigRefresher;
import org.antframework.configcenter.client.support.ListenerRegistrar;
import org.antframework.configcenter.client.support.ServerRequester;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 配置
 */
public class Config {
    // 版本
    private final AtomicLong version = new AtomicLong(0);
    // 配置项集合
    private final ConfigurableConfigProperties properties = new DefaultConfigProperties();
    // 监听器注册器
    private final ListenerRegistrar listenerRegistrar = new ListenerRegistrar();
    // 应用id
    private final String appId;
    // 配置刷新器
    private final ConfigRefresher configRefresher;

    public Config(String appId, ServerRequester serverRequester, String cacheDirPath) {
        this.appId = appId;
        configRefresher = new ConfigRefresher(
                appId,
                version,
                properties,
                listenerRegistrar,
                serverRequester,
                buildCacheFile(cacheDirPath));
        configRefresher.initConfig();
    }

    // 构建缓存文件
    private MapFile buildCacheFile(String cacheDirPath) {
        if (cacheDirPath == null) {
            return null;
        }
        String cacheFilePath = cacheDirPath + File.separator + String.format("%s.properties", appId);
        return new MapFile(cacheFilePath);
    }

    /**
     * 获取配置版本
     */
    public long getVersion() {
        return version.get();
    }

    /**
     * 获取配置项集合
     */
    public ConfigProperties getProperties() {
        return properties;
    }

    /**
     * 获取监听器注册器
     */
    public ListenerRegistrar getListenerRegistrar() {
        return listenerRegistrar;
    }

    /**
     * 获取应用id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 刷新配置（同步）
     */
    public void refresh() {
        configRefresher.refresh();
    }
}
