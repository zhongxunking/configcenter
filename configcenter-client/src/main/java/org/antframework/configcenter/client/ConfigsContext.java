/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 14:53 创建
 */
package org.antframework.configcenter.client;

import org.antframework.common.util.other.Cache;
import org.antframework.configcenter.client.support.ConfigsListener;
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
        public Config apply(String appId) {
            return new Config(appId, serverRequester, cacheDirPath);
        }
    });
    // 任务执行器
    private final TaskExecutor taskExecutor = new TaskExecutor();
    // 主体应用id
    private final String mainAppId;
    // 环境id
    private final String profileId;
    // 缓存文件夹路径（null表示不使用缓存文件功能）
    private final String cacheDirPath;
    // 服务端请求器
    private final ServerRequester serverRequester;
    // 配置监听器
    private ConfigsListener configsListener;

    /**
     * 构造配置上下文
     *
     * @param serverUrl    服务端地址
     * @param mainAppId    主体应用id
     * @param profileId    环境id
     * @param cacheDirPath 缓存文件夹路径（null表示不使用缓存文件功能（既不读取缓存文件中的配置，也不写配置到缓存文件））
     */
    public ConfigsContext(String mainAppId, String profileId, String serverUrl, String cacheDirPath) {
        if (StringUtils.isBlank(serverUrl) || StringUtils.isBlank(mainAppId) || StringUtils.isBlank(profileId)) {
            throw new IllegalArgumentException(String.format("初始化配置中客户端的参数不合法：serverUrl=%s,mainAppId=%s,profileId=%s,cacheDirPath=%s", serverUrl, mainAppId, profileId, cacheDirPath));
        }
        this.mainAppId = mainAppId;
        this.profileId = profileId;
        serverRequester = new ServerRequester(mainAppId, profileId, serverUrl);
        this.cacheDirPath = cacheDirPath == null ? null : cacheDirPath + File.separator + mainAppId + File.separator + profileId;
    }

    /**
     * 主体应用id
     */
    public String getMainAppId() {
        return mainAppId;
    }

    /**
     * 环境id
     */
    public String getProfileId() {
        return profileId;
    }

    /**
     * 获取查找过配置的应用id
     */
    public Set<String> getAppIds() {
        return Collections.unmodifiableSet(configsCache.getAllKeys());
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
     * 开始监听配置变更事件
     */
    public synchronized void listenConfigs() {
        if (configsListener != null) {
            return;
        }
        configsListener = new ConfigsListener(configsCache, serverRequester);
    }

    /**
     * 刷新配置（异步）
     */
    public void refresh() {
        for (String appId : getAppIds()) {
            taskExecutor.execute(new TaskExecutor.Task<Config>(getConfig(appId)) {
                @Override
                protected void doRun(Config target) {
                    target.refresh();
                }
            });
        }
    }

    /**
     * 关闭
     */
    public synchronized void close() {
        if (configsListener != null) {
            configsListener.close();
        }
        taskExecutor.close();
    }
}
