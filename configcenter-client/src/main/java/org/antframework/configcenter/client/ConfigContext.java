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

/**
 * 配置上下文
 */
public class ConfigContext {
    // 参数
    private ConfigParams params;
    // 属性
    private ConfigurableConfigProperties properties = new DefaultConfigProperties();
    // 服务端查询器
    private ServerQuerier serverQuerier;
    // 缓存文件处理器
    private CacheFileHandler cacheFileHandler;
    // 配置刷新器
    private PropertiesRefresher propertiesRefresher;
    // 刷新触发器
    private RefreshTrigger refreshTrigger;
    // 监听器处理器
    private ListenersHandler listenersHandler = new ListenersHandler();

    public ConfigContext(ConfigParams params) {
        this.params = params;
        serverQuerier = new ServerQuerier(params);
        cacheFileHandler = new CacheFileHandler(params);
        propertiesRefresher = new PropertiesRefresher(properties, serverQuerier, cacheFileHandler, listenersHandler);
    }

    /**
     * 获取属性
     */
    public ConfigProperties getProperties() {
        return properties;
    }

    /**
     * 获取监听器处理器
     */
    public ListenersHandler getListenersHandler() {
        return listenersHandler;
    }

    /**
     * 开始监听属性是否被修改
     */
    public void listenPropertiesModified() {
        refreshTrigger = new RefreshTrigger(propertiesRefresher, params);
    }

    /**
     * 刷新属性
     */
    public void refreshProperties() {
        propertiesRefresher.refresh();
    }

    /**
     * 关闭上下文（释放相关资源）
     */
    public void close() {
        if (refreshTrigger != null) {
            refreshTrigger.close();
        }
        serverQuerier.close();
        propertiesRefresher.close();
    }

    /**
     * 客户端初始化参数
     */
    public static class ConfigParams {
        // 环境编码
        private String profileCode;
        // 主体应用编码
        private String appCode;
        // 被查询配置的应用编码
        private String queriedAppCode;
        // 服务端地址
        private String serverUrl;
        // zookeeper地址
        private String zkUrl;
        // 缓存文件路径
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
