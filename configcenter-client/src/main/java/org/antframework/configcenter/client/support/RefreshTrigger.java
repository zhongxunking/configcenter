/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 09:59 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.common.util.file.MapFile;
import org.antframework.common.util.other.Cache;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 刷新触发器
 */
public class RefreshTrigger {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTrigger.class);
    /**
     * 元数据缓存文件名称
     */
    public static final String META_CACHE_FILE_NAME = "configcenter-meta.properties";
    // zookeeper地址在缓存文件中的key
    private static final String ZK_URLS_KEY = "zkUrls";
    // zookeeper地址分隔符
    private static final char ZK_URLS_SEPARATOR = ',';
    // 配置中心在zookeeper中的命名空间
    private static final String ZK_CONFIG_NAMESPACE = "configcenter/config";

    // 监听器缓存
    private Cache<String, NodeCache> listenersCache = new Cache<>(new Cache.Supplier<String, NodeCache>() {
        @Override
        public NodeCache get(String key) {
            return listenApp(key);
        }
    });
    // 环境id
    private String profileId;
    // 元数据请求器
    private ServerRequester.MetaRequester metaRequester;
    // 刷新器
    private Refresher refresher;
    // 缓存文件
    private MapFile cacheFile;
    // zookeeper操作类
    private ZkTemplate zkTemplate;

    public RefreshTrigger(String profileId, ServerRequester serverRequester, Refresher refresher, String cacheDir) {
        this.profileId = profileId;
        metaRequester = serverRequester.createMetaRequester();
        this.refresher = refresher;
        cacheFile = buildCacheFile(cacheDir);
        zkTemplate = buildInitZkTemplate();
    }

    // 构建缓存文件
    private MapFile buildCacheFile(String cacheDir) {
        if (cacheDir == null) {
            return null;
        }
        String cacheFile = cacheDir + File.separator + META_CACHE_FILE_NAME;
        return new MapFile(cacheFile);
    }

    // 构建初始的ZkTemplate
    private ZkTemplate buildInitZkTemplate() {
        String[] zkUrls;
        boolean fromServer = true;
        try {
            zkUrls = metaRequester.getZkUrls();
        } catch (Throwable e) {
            logger.error("从配置中心读取zookeeper地址失败：{}", e.getMessage());
            if (cacheFile == null) {
                throw e;
            }
            logger.warn("尝试从缓存文件[{}]读取zookeeper地址", cacheFile.getFilePath());
            zkUrls = StringUtils.split(cacheFile.read(ZK_URLS_KEY), ZK_URLS_SEPARATOR);
            if (zkUrls == null || zkUrls.length <= 0) {
                throw new IllegalStateException(String.format("不存在缓存文件[%s]或该缓存文件中不存在zookeeper地址", cacheFile.getFilePath()));
            }
            fromServer = false;
        }
        if (fromServer && cacheFile != null) {
            cacheFile.store(ZK_URLS_KEY, StringUtils.join(zkUrls, ZK_URLS_SEPARATOR));
        }

        return ZkTemplate.create(zkUrls, ZK_CONFIG_NAMESPACE);
    }

    /**
     * 新增需被触发刷新的应用
     *
     * @param appId 应用id
     */
    public void addTriggeredApp(String appId) {
        listenersCache.get(appId);
    }

    // 监听应用
    private NodeCache listenApp(String appId) {
        ZkTemplate.NodeListener listener = new ZkTemplate.NodeListener() {
            @Override
            public void nodeChanged() throws Exception {
                try {
                    refresher.refresh(appId);
                } catch (Throwable e) {
                    logger.error("触发刷新出错：", e);
                }
            }
        };
        return zkTemplate.listenNode(ZkTemplate.buildPath(profileId, appId), false, listener);
    }

    /**
     * 刷新zookeeper链接
     */
    public void refreshZk() {
        String[] newZkUrls = metaRequester.getZkUrls();
        if (cacheFile != null) {
            cacheFile.store(ZK_URLS_KEY, StringUtils.join(newZkUrls, ZK_URLS_SEPARATOR));
        }
        Set<String> newZks = new HashSet<>(Arrays.asList(newZkUrls));
        Set<String> oldZks = new HashSet<>(Arrays.asList(zkTemplate.getZkUrls()));
        if (!newZks.equals(oldZks)) {
            // zk地址有变更，则所有操作移到新zk上
            Set<String> appIds = new HashSet<>(listenersCache.getAllKeys());
            // 清理监听器和zk链接
            clearListenersAndZk();
            // 创建新zk链接
            zkTemplate = ZkTemplate.create(newZkUrls, ZK_CONFIG_NAMESPACE);
            // 监听应用
            for (String appId : appIds) {
                listenersCache.get(appId);
            }
        }
    }

    /**
     * 关闭（释放相关资源）
     */
    public void close() {
        clearListenersAndZk();
    }

    // 清理监听器和zk链接
    private void clearListenersAndZk() {
        for (String appId : listenersCache.getAllKeys()) {
            try {
                listenersCache.get(appId).close();
            } catch (Throwable e) {
                logger.error("关闭zookeeper监听器出错：", e);
            }
        }
        listenersCache.clear();
        try {
            zkTemplate.close();
        } catch (Throwable e) {
            logger.error("关闭zookeeper链接出错：", e);
        }
        zkTemplate = null;
    }

    /**
     * 刷新器
     */
    public interface Refresher {

        /**
         * 刷新
         *
         * @param appId 需被刷新的应用
         */
        void refresh(String appId);
    }
}
