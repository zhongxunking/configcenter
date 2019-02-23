/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 10:24 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.common.util.file.MapFile;
import org.antframework.configcenter.client.core.ChangedProperty;
import org.antframework.configcenter.client.core.ConfigurableConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 配置刷新器
 */
public class ConfigRefresher {
    private static final Logger logger = LoggerFactory.getLogger(ConfigRefresher.class);

    // 配置项集合
    private final ConfigurableConfigProperties properties;
    // 监听器注册器
    private final ListenerRegistrar listenerRegistrar;
    // 配置请求器
    private final ServerRequester.ConfigRequester configRequester;
    // 缓存文件
    private final MapFile cacheFile;

    public ConfigRefresher(ConfigurableConfigProperties properties,
                           ListenerRegistrar listenerRegistrar,
                           ServerRequester.ConfigRequester configRequester,
                           MapFile cacheFile) {
        this.properties = properties;
        this.listenerRegistrar = listenerRegistrar;
        this.configRequester = configRequester;
        this.cacheFile = cacheFile;
    }

    /**
     * 初始化配置（先从服务端读取配置，如果失败则尝试从本地缓存文件读取配置）
     */
    public synchronized void initConfig() {
        Map<String, String> newProperties;
        boolean fromServer = true;
        try {
            newProperties = configRequester.findConfig();
        } catch (Throwable e) {
            logger.error("从配置中心读取配置失败：{}", e.getMessage());
            if (cacheFile == null) {
                throw e;
            }
            logger.warn("尝试从缓存文件[{}]读取配置", cacheFile.getFilePath());
            if (!cacheFile.exists()) {
                throw new IllegalStateException(String.format("不存在缓存文件[%s]", cacheFile.getFilePath()));
            }
            newProperties = cacheFile.readAll();
            fromServer = false;
        }
        if (fromServer && cacheFile != null) {
            cacheFile.replace(newProperties);
        }
        properties.replaceProperties(newProperties);
    }

    /**
     * 刷新配置
     */
    public synchronized void refresh() {
        Map<String, String> newProperties = configRequester.findConfig();
        if (cacheFile != null) {
            cacheFile.replace(newProperties);
        }
        List<ChangedProperty> changedProperties = properties.replaceProperties(newProperties);
        listenerRegistrar.onChange(changedProperties);
    }
}
