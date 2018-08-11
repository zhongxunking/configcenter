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
import org.antframework.configcenter.client.support.ServerRequester;

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
    // 服务端请求器
    private ServerRequester serverRequester;
    // 配置刷新器
    private ConfigRefresher configRefresher;
    // 刷新触发器
    private RefreshTrigger refreshTrigger;

    public ConfigContext(InitParams initParams) {
        this.initParams = initParams;
        serverRequester = new ServerRequester(initParams);
        configRefresher = new ConfigRefresher(properties, listenerRegistrar, serverRequester, initParams.getCacheFilePath());
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
    public synchronized void listenConfigChanged() {
        if (refreshTrigger == null) {
            refreshTrigger = new RefreshTrigger(configRefresher, serverRequester.getZkUrls(), initParams);
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
        // 必填：主体应用id
        private String mainAppId;
        // 必填：被查询配置的应用id
        private String queriedAppId;
        // 必填：环境id
        private String profileId;
        // 必填：服务端地址
        private String serverUrl;
        // 选填：缓存文件路径（不填表示：不使用缓存文件功能，既不读取缓存文件中的配置，也不写配置到缓存文件）
        private String cacheFilePath;

        public String getMainAppId() {
            return mainAppId;
        }

        public void setMainAppId(String mainAppId) {
            this.mainAppId = mainAppId;
        }

        public String getQueriedAppId() {
            return queriedAppId;
        }

        public void setQueriedAppId(String queriedAppId) {
            this.queriedAppId = queriedAppId;
        }

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
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
    }
}
