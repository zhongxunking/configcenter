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

/**
 * 刷新触发器
 */
public class RefreshTrigger {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTrigger.class);
    // 配置中心在zookeeper的命名空间
    private static final String ZK_CONFIG_NAMESPACE = "configcenter/config";

    // 配置刷新器
    private ConfigRefresher refresher;
    // zookeeper操作类
    private ZkTemplate zkTemplate;
    // 被监听的节点
    private NodeCache nodeCache;

    public RefreshTrigger(ConfigRefresher refresher, ConfigContext.InitParams initParams) {
        this.refresher = refresher;
        if (initParams.getZkUrls() == null || initParams.getZkUrls().length == 0) {
            throw new IllegalArgumentException("未设置zookeeper地址");
        }
        zkTemplate = ZkTemplate.create(initParams.getZkUrls(), ZK_CONFIG_NAMESPACE);
        nodeCache = listenNode(initParams.getProfileId(), initParams.getQueriedAppId());
    }

    /**
     * 关闭（释放相关资源）
     */
    public void close() {
        try {
            nodeCache.close();
        } catch (IOException e) {
            logger.error("关闭节点监听器出错：", e);
        }
        zkTemplate.close();
    }

    // 监听指定节点
    private NodeCache listenNode(String profileId, String appId) {
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
        return zkTemplate.listenNode(ZkTemplate.buildPath(profileId, appId), false, listener);
    }
}
