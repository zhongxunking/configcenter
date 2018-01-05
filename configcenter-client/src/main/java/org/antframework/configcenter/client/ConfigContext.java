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
    private InitParams initParams;
    // 配置刷新器
    private ConfigRefresher configRefresher;
    // 刷新触发器
    private RefreshTrigger refreshTrigger;

    public ConfigContext(InitParams initParams) {
        this.initParams = initParams;
        configRefresher = new ConfigRefresher(properties, listenerRegistrar, initParams);
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
            refreshTrigger = new RefreshTrigger(configRefresher, initParams);
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
        // 必填：主体应用编码
        private String appCode;
        // 必填：被查询配置的应用编码
        private String queriedAppCode;
        // 必填：环境编码
        private String profileCode;
        // 必填：服务端地址
        private String serverUrl;
        // 选填：缓存文件路径（不填表示：不使用缓存文件功能，既不读取缓存文件中的配置，也不写配置到缓存文件）
        private String cacheFilePath;
        // 选填：配置中心使用的zookeeper地址（如果不需要调用listenConfigModified()触发监听配置是否被修改，可以不用填）
        private String[] zkUrls;

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

        public String getProfileCode() {
            return profileCode;
        }

        public void setProfileCode(String profileCode) {
            this.profileCode = profileCode;
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

        public String[] getZkUrls() {
            return zkUrls;
        }

        public void setZkUrls(String... zkUrls) {
            this.zkUrls = zkUrls;
        }
    }
}
