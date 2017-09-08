/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-07 16:46 创建
 */
package org.antframework.configcenter.biz;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * zookeeper操作类
 */
public class ZkOperations {
    /**
     * 路径中节点分隔符
     */
    public static final char NODE_SEPARATOR = '/';

    // zookeeper客户端
    private CuratorFramework zkClient;

    public ZkOperations(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }

    /**
     * 创建节点
     *
     * @param path 路径中任何一个节点如果不存在，则创建
     */
    public void createNodesByPath(String path) {
        try {
            StringBuilder pathBuilder = new StringBuilder();
            for (String node : StringUtils.split(path, NODE_SEPARATOR)) {
                pathBuilder.append(NODE_SEPARATOR).append(node);
                if (zkClient.checkExists().forPath(pathBuilder.toString()) != null) {
                    continue;
                }
                zkClient.create().withMode(CreateMode.PERSISTENT).forPath(pathBuilder.toString());
            }
        } catch (Throwable e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    /**
     * 给节点设置数据
     *
     * @param path 节点路径
     * @param data 数据
     */
    public void setData(String path, byte[] data) {
        try {
            zkClient.setData().forPath(path, data);
        } catch (Throwable e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    /**
     * 构建路径
     *
     * @param nodes 路径中从前到后的节点
     */
    public static String buildPath(String... nodes) {
        if (nodes == null) {
            return null;
        }
        StringBuilder pathBuilder = new StringBuilder();
        for (String node : nodes) {
            pathBuilder.append(NODE_SEPARATOR).append(node);
        }
        return pathBuilder.toString();
    }

}
