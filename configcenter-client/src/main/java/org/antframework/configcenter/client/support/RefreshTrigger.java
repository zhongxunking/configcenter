/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 09:59 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.client.ConfigContext;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 刷新触发器
 */
public class RefreshTrigger {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTrigger.class);
    // 配置中心在zookeeper的命名空间
    private static final String ZK_CONFIG_NAMESPACE = "configcenter/config";
    // 需监听的公共节点
    private static final String COMMON_NODE = "common";

    // 配置刷新器
    private ConfigRefresher refresher;
    // zookeeper操作类
    private ZkTemplate zkTemplate;
    // 被监听的节点
    private List<NodeCache> nodeCaches;

    public RefreshTrigger(ConfigRefresher refresher, ConfigContext.InitParams initParams) {
        this.refresher = refresher;
        if (initParams.getZkUrls() == null || initParams.getZkUrls().length == 0) {
            throw new IllegalArgumentException("未设置zookeeper地址");
        }
        zkTemplate = ZkTemplate.create(initParams.getZkUrls(), ZK_CONFIG_NAMESPACE);
        nodeCaches = listenNodes(initParams.getProfileCode(), new String[]{COMMON_NODE, initParams.getQueriedAppCode()});
    }

    /**
     * 关闭（释放相关资源）
     */
    public void close() {
        for (NodeCache nodeCache : nodeCaches) {
            try {
                nodeCache.close();
            } catch (IOException e) {
                logger.error("关闭节点监听器出错：", e);
            }
        }
        zkTemplate.close();
    }

    // 监听指定节点
    private List<NodeCache> listenNodes(String profileCode, String[] appCodes) {
        ZkTemplate.NodeListener listener = new ZkTemplate.NodeListener() {
            @Override
            public void nodeChanged() throws Exception {
                try {
                    refresher.refresh();
                } catch (Throwable e) {
                    logger.error("触发刷新出错：", e);
                }
            }
        };
        List<NodeCache> nodeCaches = new ArrayList<>();
        for (String appCode : appCodes) {
            NodeCache nodeCache = zkTemplate.listenNode(ZkTemplate.buildPath(profileCode, appCode), false, listener);
            nodeCaches.add(nodeCache);
        }
        return nodeCaches;
    }
}
