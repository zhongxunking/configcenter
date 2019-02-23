/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 14:53 创建
 */
package org.antframework.configcenter.client;

import org.antframework.common.util.other.Cache;
import org.antframework.configcenter.client.support.RefreshTrigger;
import org.antframework.configcenter.client.support.ServerRequester;
import org.antframework.configcenter.client.support.TaskExecutor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

/**
 * 配置上下文
 */
public class ConfigsContext {
    // config缓存
    private final Cache<String, Config> configsCache = new Cache<>(new Function<String, Config>() {
        @Override
        public Config apply(String key) {
            Config config = new Config(key, serverRequester, initParams.calcCacheDir());
            if (refreshTrigger != null) {
                refreshTrigger.addApp(key);
            }
            return config;
        }
    });
    // 任务执行器
    private final TaskExecutor taskExecutor = new TaskExecutor();
    // 初始化参数
    private final InitParams initParams;
    // 服务端请求器
    private final ServerRequester serverRequester;
    // 刷新触发器
    private RefreshTrigger refreshTrigger;

    public ConfigsContext(InitParams initParams) {
        initParams.check();
        this.initParams = initParams;
        serverRequester = new ServerRequester(initParams.serverUrl, initParams.mainAppId, initParams.profileId);
    }

    /**
     * 获取配置
     *
     * @param appId 被查询配置的应用id
     * @return 指定应用的配置
     */
    public Config getConfig(String appId) {
        return configsCache.get(appId);
    }

    /**
     * 开始监听配置是否被修改
     */
    public synchronized void listenConfigChanged() {
        if (refreshTrigger != null) {
            return;
        }
        RefreshTrigger.Refresher refresher = new RefreshTrigger.Refresher() {
            @Override
            public void refresh(String appId) {
                refreshConfig(appId);
            }
        };
        refreshTrigger = new RefreshTrigger(initParams.profileId, serverRequester, refresher, initParams.calcCacheDir());
        for (String appId : getAppIds()) {
            refreshTrigger.addApp(appId);
        }
    }

    /**
     * 刷新配置和zookeeper链接（异步）
     */
    public void refresh() {
        for (String appId : getAppIds()) {
            refreshConfig(appId);
            if (refreshTrigger != null) {
                refreshTrigger.addApp(appId);
            }
        }
        if (refreshTrigger != null) {
            taskExecutor.execute(new TaskExecutor.Task<RefreshTrigger>(refreshTrigger) {
                @Override
                protected void doRun(RefreshTrigger target) {
                    target.refreshZk();
                }
            });
        }
    }

    /**
     * 获取所有已查找配置的应用id
     */
    public Set<String> getAppIds() {
        return Collections.unmodifiableSet(configsCache.getAllKeys());
    }

    /**
     * 关闭（释放相关资源）
     */
    public synchronized void close() {
        if (refreshTrigger != null) {
            refreshTrigger.close();
        }
        taskExecutor.close();
    }

    // 刷新配置
    private void refreshConfig(String appId) {
        taskExecutor.execute(new TaskExecutor.Task<Config>(configsCache.get(appId)) {
            @Override
            protected void doRun(Config target) {
                target.refresh();
            }
        });
    }

    /**
     * 客户端初始化参数
     */
    public static class InitParams {
        // 缓存文件夹附加路径
        private static final String CACHE_DIR_APPENDING = "configcenter";

        // 必填：服务端地址
        private String serverUrl;
        // 必填：主体应用id
        private String mainAppId;
        // 必填：环境id
        private String profileId;
        // 选填：缓存文件夹路径（不填表示：不使用缓存文件功能，既不读取缓存文件中的配置，也不写配置到缓存文件）
        private String cacheDir;

        public String getServerUrl() {
            return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public String getMainAppId() {
            return mainAppId;
        }

        public void setMainAppId(String mainAppId) {
            this.mainAppId = mainAppId;
        }

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        public String getCacheDir() {
            return cacheDir;
        }

        public void setCacheDir(String cacheDir) {
            this.cacheDir = cacheDir;
        }

        // 校验参数
        void check() {
            if (StringUtils.isBlank(serverUrl) || StringUtils.isBlank(mainAppId) || StringUtils.isBlank(profileId)) {
                throw new IllegalArgumentException("服务端地址、主体应用id、环境id为必传项");
            }
        }

        // 计算真正使用的缓存文件夹路径
        String calcCacheDir() {
            String cacheDirToUse = cacheDir;
            if (cacheDirToUse != null) {
                if (!cacheDirToUse.endsWith(File.separator)) {
                    cacheDirToUse += File.separator;
                }
                cacheDirToUse += CACHE_DIR_APPENDING + File.separator + mainAppId + File.separator + profileId;
            }
            return cacheDirToUse;
        }
    }
}
