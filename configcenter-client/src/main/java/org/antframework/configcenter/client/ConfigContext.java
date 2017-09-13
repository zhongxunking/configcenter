/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 14:53 创建
 */
package org.antframework.configcenter.client;

import org.antframework.configcenter.client.core.ConfigurableConfigProperties;
import org.antframework.configcenter.client.core.DefaultConfigProperties;
import org.antframework.configcenter.client.support.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ConfigContext {
    private static final Logger logger = LoggerFactory.getLogger(ConfigContext.class);

    private ConfigurableConfigProperties configProperties;
    private ServerQuerier serverQuerier;
    private CacheFileHandler cacheFileHandler;
    private ConfigRefresher configRefresher;
    private RefreshTrigger refreshTrigger;
    private ListenersHandler listenersHandler = new ListenersHandler();

    public ConfigContext(String profileCode, String appCode, String queriedAppCode, String serverUrl, String zkUrl, String cacheFilePath) {
        this.configProperties = new DefaultConfigProperties();
        this.serverQuerier = new ServerQuerier(serverUrl, profileCode, appCode, queriedAppCode);
        this.cacheFileHandler = new CacheFileHandler(cacheFilePath);
        this.configRefresher = new ConfigRefresher(this.configProperties, this.serverQuerier, this.cacheFileHandler, this.listenersHandler);
        this.refreshTrigger = new RefreshTrigger(this, zkUrl, profileCode, queriedAppCode);
    }

    public ConfigProperties getProperties() {
        return configProperties;
    }

    public ListenersHandler getListenersHandler() {
        return listenersHandler;
    }

    public void refreshProperties() {
        configRefresher.refresh();
    }

    public void close() {
        refreshTrigger.close();
        serverQuerier.close();
        configRefresher.close();
    }
}
