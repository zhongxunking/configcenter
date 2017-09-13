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

    private ConfigParams params;
    private ConfigurableConfigProperties configProperties = new DefaultConfigProperties();
    private ServerQuerier serverQuerier;
    private CacheFileHandler cacheFileHandler;
    private ConfigRefresher configRefresher;
    private RefreshTrigger refreshTrigger;
    private ListenersHandler listenersHandler = new ListenersHandler();

    public ConfigContext(ConfigParams params) {
        this.params = params;
        serverQuerier = new ServerQuerier(params);
        cacheFileHandler = new CacheFileHandler(params);
        configRefresher = new ConfigRefresher(configProperties, serverQuerier, cacheFileHandler, listenersHandler);
    }

    public ConfigProperties getConfigProperties() {
        return configProperties;
    }

    public ListenersHandler getListenersHandler() {
        return listenersHandler;
    }

    public void startListenPropertiesModified() {
        refreshTrigger = new RefreshTrigger(this, params);
    }

    public void refreshProperties() {
        configRefresher.refresh();
    }

    public void close() {
        refreshTrigger.close();
        serverQuerier.close();
        configRefresher.close();
    }

    public static class ConfigParams {
        private String profileCode;
        private String appCode;
        private String queriedAppCode;
        private String serverUrl;
        private String zkUrl;
        private String cacheFilePath;

        public String getProfileCode() {
            return profileCode;
        }

        public void setProfileCode(String profileCode) {
            this.profileCode = profileCode;
        }

        public String getAppCode() {
            return appCode;
        }

        public void setAppCode(String appCode) {
            this.appCode = appCode;
        }

        public String getQueriedAppCode() {
            return queriedAppCode;
        }

        public void setQueriedAppCode(String queriedAppCode) {
            this.queriedAppCode = queriedAppCode;
        }

        public String getServerUrl() {
            return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public String getZkUrl() {
            return zkUrl;
        }

        public void setZkUrl(String zkUrl) {
            this.zkUrl = zkUrl;
        }

        public String getCacheFilePath() {
            return cacheFilePath;
        }

        public void setCacheFilePath(String cacheFilePath) {
            this.cacheFilePath = cacheFilePath;
        }
    }
}
