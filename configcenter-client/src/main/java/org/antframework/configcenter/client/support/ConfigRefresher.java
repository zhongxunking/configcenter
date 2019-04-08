/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 10:24 创建
 */
package org.antframework.configcenter.client.support;

import lombok.extern.slf4j.Slf4j;
import org.antframework.common.util.file.MapFile;
import org.antframework.configcenter.client.core.ChangedProperty;
import org.antframework.configcenter.client.core.ConfigurableConfigProperties;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 配置刷新器
 */
@Slf4j
public class ConfigRefresher {
    // 配置版本的key
    private static final String VERSION_KEY = ConfigRefresher.class.getName() + "#configVersion";

    // 版本
    private final AtomicLong version;
    // 配置项集合
    private final ConfigurableConfigProperties properties;
    // 监听器注册器
    private final ListenerRegistrar listenerRegistrar;
    // 配置请求器
    private final ServerRequester.ConfigRequester configRequester;
    // 缓存文件
    private final MapFile cacheFile;

    public ConfigRefresher(String appId,
                           AtomicLong version,
                           ConfigurableConfigProperties properties,
                           ListenerRegistrar listenerRegistrar,
                           ServerRequester serverRequester,
                           MapFile cacheFile) {
        this.version = version;
        this.properties = properties;
        this.listenerRegistrar = listenerRegistrar;
        this.configRequester = serverRequester.createConfigRequester(appId, VERSION_KEY);
        this.cacheFile = cacheFile;
    }

    /**
     * 初始化配置（先从服务端读取配置，如果失败则尝试从本地缓存文件读取配置）
     */
    public synchronized void initConfig() {
        Map<String, String> config;
        boolean fromServer = true;
        try {
            config = configRequester.findConfig();
        } catch (Throwable e) {
            log.error("从configcenter读取配置失败：{}", e.getMessage());
            if (cacheFile == null) {
                throw e;
            }
            log.warn("尝试从缓存文件[{}]读取配置", cacheFile.getFilePath());
            if (!cacheFile.exists()) {
                throw new IllegalStateException(String.format("不存在缓存文件[%s]", cacheFile.getFilePath()));
            }
            config = cacheFile.readAll();
            fromServer = false;
        }
        if (fromServer && cacheFile != null) {
            cacheFile.replace(config);
            log.debug("configcenter配置已缓存到：{}", cacheFile.getFilePath());
        }
        version.set(Long.parseLong(config.get(VERSION_KEY)));
        config.remove(VERSION_KEY);
        properties.replaceProperties(config);
    }

    /**
     * 刷新配置
     */
    public synchronized void refresh() {
        Map<String, String> config = configRequester.findConfig();
        if (cacheFile != null) {
            cacheFile.replace(config);
            log.debug("configcenter配置已缓存到：{}", cacheFile.getFilePath());
        }
        version.set(Long.parseLong(config.get(VERSION_KEY)));
        config.remove(VERSION_KEY);
        List<ChangedProperty> changedProperties = properties.replaceProperties(config);
        listenerRegistrar.onChange(changedProperties);
    }
}
