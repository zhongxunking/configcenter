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
import org.antframework.configcenter.client.support.ConfigRefresher;
import org.antframework.configcenter.client.support.ListenerRegistrar;
import org.antframework.configcenter.client.support.RefreshTrigger;

/**
 * 配置上下文
 */
public class ConfigContext {
    // 配置属性
    private ConfigurableConfigProperties properties = new DefaultConfigProperties();
    // 监听器注册器
    private ListenerRegistrar listenerRegistrar = new ListenerRegistrar();
    // 初始化参数
    private InitParams params;
    // 配置刷新器
    private ConfigRefresher configRefresher;
    // 刷新触发器
    private RefreshTrigger refreshTrigger;

    public ConfigContext(InitParams params) {
        this.params = params;
        configRefresher = new ConfigRefresher(properties, listenerRegistrar, params);
        configRefresher.initConfig();
    }

    /**
     * 获取属性
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
     * 开始监听配置是否被修改
     */
    public synchronized void listenConfigModified() {
        if (refreshTrigger == null) {
            refreshTrigger = new RefreshTrigger(configRefresher, params);
        }
    }

    /**
     * 刷新配置（异步）
     */
    public void refreshConfig() {
        configRefresher.refresh();
    }

    /**
     * 关闭（释放相关资源）
     */
    public void close() {
        if (refreshTrigger != null) {
            refreshTrigger.close();
        }
        configRefresher.close();
    }

    /**
     * 客户端初始化参数
     */
    public static class InitParams {
        // 环境编码（必须）
        private String profileCode;
        // 主体应用编码（必须）
        private String appCode;
        // 被查询配置的应用编码（必须）
        private String queriedAppCode;
        // 服务端地址（必须）
        private String serverUrl;
        // 缓存文件路径（必须）
        private String cacheFilePath;
        // zookeeper地址（多个zookeeper地址的话以“,”相隔。如果不需要监听配置是否被修改，可以不用传）
        private String zkUrl;

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

        public String getCacheFilePath() {
            return cacheFilePath;
        }

        public void setCacheFilePath(String cacheFilePath) {
            this.cacheFilePath = cacheFilePath;
        }

        public String getZkUrl() {
            return zkUrl;
        }

        public void setZkUrl(String zkUrl) {
            this.zkUrl = zkUrl;
        }
    }
}
