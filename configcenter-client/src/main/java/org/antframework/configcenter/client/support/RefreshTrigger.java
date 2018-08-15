/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 09:59 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.common.util.file.MapFile;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.client.ConfigContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 刷新触发器
 */
public class RefreshTrigger {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTrigger.class);
    // 缓存文件后缀
    private static final String CACHE_FILE_SUFFIX = "-meta";
    // zookeeper地址在缓存文件中的key
    private static final String ZK_URLS_KEY = "zkUrls";
    // zookeeper地址分隔符
    private static final char ZK_URLS_SEPARATOR = ',';
    // 配置中心在zookeeper中的命名空间
    private static final String ZK_CONFIG_NAMESPACE = "configcenter/config";

    // 执行刷新zookeeper连接任务的线程池
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            0,
            1,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.DiscardPolicy());
    // 刷新zookeeper连接任务
    private RefreshZkTask refreshZkTask = new RefreshZkTask();
    // 配置刷新器
    private ConfigRefresher configRefresher;
    // 服务端请求器
    private ServerRequester serverRequester;
    // 缓存文件
    private MapFile cacheFile;
    // 监听的节点
    private String nodePath;
    // 触发执行器
    private TriggerExecutor executor;

    public RefreshTrigger(ConfigRefresher configRefresher, ServerRequester serverRequester, ConfigContext.InitParams initParams) {
        this.configRefresher = configRefresher;
        this.serverRequester = serverRequester;
        if (initParams.getCacheFile() != null) {
            cacheFile = new MapFile(initParams.getCacheFile() + CACHE_FILE_SUFFIX);
        }
        nodePath = ZkTemplate.buildPath(initParams.getProfileId(), initParams.getQueriedAppId());
        executor = new TriggerExecutor(getInitZkUrls());
    }

    // 获取初始化的zookeeper地址
    private String[] getInitZkUrls() {
        String[] zkUrls;
        boolean fromServer = true;
        try {
            zkUrls = serverRequester.getZkUrls();
        } catch (Throwable e) {
            logger.error("从配置中心读取zookeeper地址失败：{}", e.getMessage());
            if (cacheFile == null) {
                throw e;
            }
            logger.warn("尝试从缓存文件读取zookeeper地址");
            zkUrls = StringUtils.split(cacheFile.read(ZK_URLS_KEY), ZK_URLS_SEPARATOR);
            if (zkUrls == null) {
                throw new IllegalStateException(String.format("不存在缓存文件[%s]或该缓存文件中不存在zookeeper地址", cacheFile.getFilePath()));
            }
            fromServer = false;
        }
        if (fromServer && cacheFile != null) {
            cacheFile.store(ZK_URLS_KEY, StringUtils.join(zkUrls, ZK_URLS_SEPARATOR));
        }
        return zkUrls;
    }

    /**
     * 刷新zookeeper链接
     */
    public void refreshZk() {
        threadPool.execute(refreshZkTask);
    }

    /**
     * 关闭（释放相关资源）
     */
    public void close() {
        executor.close();
        threadPool.shutdown();
    }

    // 刷新zookeeper链接的任务
    private class RefreshZkTask implements Runnable {
        @Override
        public void run() {
            try {
                String[] newZkUrls = serverRequester.getZkUrls();
                if (isChanged(newZkUrls)) {
                    executor.close();
                    executor = new TriggerExecutor(newZkUrls);
                }
            } catch (Throwable e) {
                logger.error("刷新zookeeper链接出错：{}", e.getMessage());
            }
        }

        // zookeeper地址是否有变化
        private boolean isChanged(String[] newZkUrls) {
            Set<String> newZks = new HashSet<>(Arrays.asList(newZkUrls));
            Set<String> oldZks = new HashSet<>(Arrays.asList(executor.getZkUrls()));
            return !newZks.equals(oldZks);
        }
    }

    // 触发执行器
    private class TriggerExecutor {
        // zookeeper地址
        private String[] zkUrls;
        // zookeeper操作类
        private ZkTemplate zkTemplate;
        // 被监听的节点
        private NodeCache nodeCache;

        public TriggerExecutor(String[] zkUrls) {
            this.zkUrls = zkUrls;
            zkTemplate = ZkTemplate.create(zkUrls, ZK_CONFIG_NAMESPACE);
            nodeCache = listenZkNode();
        }

        // 监听zookeeper节点
        private NodeCache listenZkNode() {
            ZkTemplate.NodeListener listener = new ZkTemplate.NodeListener() {
                @Override
                public void nodeChanged() throws Exception {
                    try {
                        configRefresher.refresh();
                    } catch (Throwable e) {
                        logger.error("触发刷新出错：", e);
                    }
                }
            };
            return zkTemplate.listenNode(nodePath, false, listener);
        }

        /**
         * 获取zookeeper地址
         */
        public String[] getZkUrls() {
            return zkUrls;
        }

        /**
         * 关闭（释放相关资源）
         */
        public void close() {
            try {
                nodeCache.close();
            } catch (IOException e) {
                logger.error("关闭zookeeper节点监听器出错：", e);
            }
            try {
                zkTemplate.close();
            } catch (Throwable e) {
                logger.error("关闭zookeeper链接出错：", e);
            }
        }
    }
}
