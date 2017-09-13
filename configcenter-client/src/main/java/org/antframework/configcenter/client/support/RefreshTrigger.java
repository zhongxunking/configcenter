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
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RefreshTrigger {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTrigger.class);
    public static final String ZK_CONFIG_NAMESPACE = "configcenter/config";
    public static final String COMMON_NODE = "common";

    private ConfigContext configContext;
    private ZkTemplate zkTemplate;
    private List<NodeCache> nodeCaches;

    public RefreshTrigger(ConfigContext configContext, ConfigContext.ConfigParams configParams) {
        this.configContext = configContext;
        this.zkTemplate = buildZkTemplate(configParams.getZkUrl());
        this.nodeCaches = buildNodeCaches(configParams.getProfileCode(), new String[]{COMMON_NODE, configParams.getQueriedAppCode()});
    }

    public void close() {
        for (NodeCache nodeCache : nodeCaches) {
            try {
                nodeCache.close();
            } catch (IOException e) {
                logger.error("关闭节点监听器出错：", e);
            }
        }
        zkTemplate.getZkClient().close();
    }

    private ZkTemplate buildZkTemplate(String zkUrl) {
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkUrl)
                .namespace(ZK_CONFIG_NAMESPACE)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
        zkClient.start();

        return new ZkTemplate(zkClient);
    }

    private List<NodeCache> buildNodeCaches(String profileCode, String[] appCodes) {
        ZkTemplate.NodeListener listener = new ZkTemplate.NodeListener() {
            @Override
            public void nodeChanged() throws Exception {
                configContext.refreshProperties();
            }
        };
        List<NodeCache> nodeCaches = new ArrayList<>();
        for (String appCode : appCodes) {
            NodeCache nodeCache = zkTemplate.listenNode(ZkTemplate.buildPath(profileCode, appCode), listener);
            nodeCaches.add(nodeCache);
        }
        return nodeCaches;
    }
}
