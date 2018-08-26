/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-19 00:13 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.common.util.file.MapFile;
import org.antframework.configcenter.client.ConfigProperties;
import org.antframework.configcenter.client.core.ConfigurableConfigProperties;
import org.antframework.configcenter.client.core.DefaultConfigProperties;

import java.io.File;

/**
 * 配置
 */
public class Config {
    // 配置属性
    private ConfigurableConfigProperties properties = new DefaultConfigProperties();
    // 监听器注册器
    private ListenerRegistrar listenerRegistrar = new ListenerRegistrar();
    // 应用id
    private String appId;
    // 配置刷新器
    private ConfigRefresher configRefresher;

    public Config(String appId, ServerRequester serverRequester, String cacheDir) {
        this.appId = appId;
        configRefresher = new ConfigRefresher(properties, listenerRegistrar, serverRequester.createConfigRequester(appId), buildCacheFile(cacheDir));
        configRefresher.initConfig();
    }

    // 构建缓存文件
    private MapFile buildCacheFile(String cacheDir) {
        if (cacheDir == null) {
            return null;
        }
        String cacheFile = cacheDir + File.separator + String.format("%s.properties", appId);
        return new MapFile(cacheFile);
    }

    /**
     * 获取配置属性
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
     * 刷新配置
     */
    public synchronized void refresh() {
        configRefresher.refresh();
    }
}
