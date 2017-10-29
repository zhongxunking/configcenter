/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 10:24 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.client.core.ConfigurableConfigProperties;
import org.antframework.configcenter.client.core.ModifiedProperty;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 配置刷新器
 */
public class ConfigRefresher {
    private static final Logger logger = LoggerFactory.getLogger(ConfigRefresher.class);
    // 刷新配置
    private static final Object REFRESH_ELEMENT = new Object();
    // 停止刷新
    private static final Object STOP_ELEMENT = new Object();

    // 配置属性
    private ConfigurableConfigProperties properties;
    // 监听器注册器
    private ListenerRegistrar listenerRegistrar;
    // 服务端查询器
    private ServerQuerier serverQuerier;
    // 缓存文件处理器
    private CacheFileHandler cacheFileHandler;
    // 刷新配置的线程
    private RefreshThread refreshThread;
    // 触发配置刷新的队列
    private BlockingQueue queue = new LinkedBlockingQueue();

    public ConfigRefresher(ConfigurableConfigProperties properties, ListenerRegistrar listenerRegistrar, ConfigContext.InitParams initParams) {
        this.properties = properties;
        this.listenerRegistrar = listenerRegistrar;
        this.serverQuerier = new ServerQuerier(initParams);
        if (initParams.getCacheFilePath() != null) {
            this.cacheFileHandler = new CacheFileHandler(initParams.getCacheFilePath());
        }
        this.refreshThread = new RefreshThread();
        this.refreshThread.start();
    }

    /**
     * 刷新配置
     */
    public void refresh() {
        try {
            if (!queue.contains(REFRESH_ELEMENT)) {
                queue.put(REFRESH_ELEMENT);
            }
        } catch (InterruptedException e) {
            ExceptionUtils.rethrow(e);
        }
    }

    /**
     * 关闭（释放相关资源）
     */
    public void close() {
        try {
            queue.put(STOP_ELEMENT);
        } catch (InterruptedException e) {
            logger.error("关闭刷新配置的线程失败");
        }
        serverQuerier.close();
    }

    /**
     * 初始化配置
     * （先从服务端读取配置，如果失败则尝试从本地缓存文件读取配置）
     */
    public void initConfig() {
        try {
            Map<String, String> newProperties;
            boolean fromServer = true;
            try {
                newProperties = serverQuerier.queryConfig();
            } catch (Throwable e) {
                logger.error("从配置中心读取配置失败：{}", e.getMessage());
                if (cacheFileHandler != null) {
                    logger.warn("尝试从缓存文件读取配置");
                    newProperties = cacheFileHandler.readConfig();
                    fromServer = false;
                } else {
                    throw e;
                }
            }
            properties.replaceProperties(newProperties);
            if (fromServer && cacheFileHandler != null) {
                cacheFileHandler.storeConfig(newProperties);
            }
        } catch (Throwable e) {
            close();
            throw e;
        }
    }

    // 刷新配置的线程
    private class RefreshThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Object element = queue.take();
                    if (element == STOP_ELEMENT) {
                        break;
                    }
                    Map<String, String> newProperties = serverQuerier.queryConfig();
                    List<ModifiedProperty> modifiedProperties = properties.replaceProperties(newProperties);
                    if (cacheFileHandler != null) {
                        cacheFileHandler.storeConfig(newProperties);
                    }
                    listenerRegistrar.configModified(modifiedProperties);
                } catch (Throwable e) {
                    logger.error("刷新配置出错：{}", e.getMessage());
                }
            }
        }
    }
}
