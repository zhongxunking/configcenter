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

import java.util.List;

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
    public void createNode(String path) {
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
     * 删除节点（如果该节点存在子节点，则会递归删除子节点）
     *
     * @param path 节点路径
     */
    public void deleteNode(String path) {
        try {
            if (zkClient.checkExists().forPath(path) == null) {
                return;
            }
            List<String> children = zkClient.getChildren().forPath(path);
            for (String child : children) {
                deleteNode(buildPath(path, child));
            }
            zkClient.delete().forPath(path);
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
     * @param pathParts 路径片段
     */
    public static String buildPath(String... pathParts) {
        if (pathParts == null) {
            return null;
        }
        StringBuilder pathBuilder = new StringBuilder();
        for (String pathPart : pathParts) {
            if (!pathPart.startsWith(Character.toString(NODE_SEPARATOR))) {
                pathBuilder.append(NODE_SEPARATOR);
            }
            pathBuilder.append(pathPart);
        }
        return pathBuilder.toString();
    }
}
