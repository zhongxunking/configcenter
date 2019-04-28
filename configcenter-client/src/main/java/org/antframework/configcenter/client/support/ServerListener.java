/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 09:59 创建
 */
package org.antframework.configcenter.client.support;

import lombok.extern.slf4j.Slf4j;
import org.antframework.common.util.other.Cache;
import org.antframework.configcenter.client.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 服务端监听器
 */
@Slf4j
public class ServerListener {
    // 配置缓存
    private final Cache<String, Config> configsCache;
    // 监听请求器
    private final ServerRequester.ListenRequester listenRequester;
    // 监听任务
    private final ListenTask listenTask;

    public ServerListener(Cache<String, Config> configsCache, ServerRequester serverRequester) {
        this.configsCache = configsCache;
        listenRequester = serverRequester.createListenRequester();
        listenTask = new ListenTask();
        listenTask.start();
    }

    // 监听服务端的配置
    private Set<String> listen() {
        List<String> appIds = new ArrayList<>();
        List<Long> configVersions = new ArrayList<>();
        for (String appId : configsCache.getAllKeys()) {
            appIds.add(appId);
            configVersions.add(configsCache.get(appId).getVersion());
        }
        return listenRequester.listen(appIds, configVersions);
    }

    // 刷新配置
    private void refresh(Set<String> appIds) {
        for (String appId : appIds) {
            configsCache.get(appId).refresh();
        }
    }

    /**
     * 关闭
     */
    public void close() {
        listenTask.close();
    }

    // 监听任务
    private class ListenTask extends Thread {
        // 是否关闭
        private volatile boolean closed = false;

        @Override
        public void run() {
            while (!closed) {
                if (configsCache.size() <= 0) {
                    sleep();
                } else {
                    try {
                        refresh(listen());
                    } catch (Throwable e) {
                        log.error("监听configcenter服务端的配置出错：{}", e.toString());
                        sleep();
                    }
                }
            }
        }

        // 睡眠
        private void sleep() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // 忽略
            }
        }

        /**
         * 关闭
         */
        void close() {
            closed = true;
        }
    }
}
