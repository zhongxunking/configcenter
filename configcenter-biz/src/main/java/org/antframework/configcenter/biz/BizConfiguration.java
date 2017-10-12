/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:42 创建
 */
package org.antframework.configcenter.biz;

import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.facade.constant.ZkConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * biz层配置
 */
@Configuration
public class BizConfiguration {

    @Value("${config.zookeeper.url}")
    @NotBlank
    private String zkUrl;

    // zookeeper客户端
    @Bean
    public CuratorFramework zkClient() throws InterruptedException {
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkUrl)
                .namespace(ZkConstant.ZK_CONFIG_NAMESPACE)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
        zkClient.start();

        return zkClient;
    }

    // zookeeper操作类
    @Bean
    public ZkTemplate zkTemplate(CuratorFramework zkClient) {
        return new ZkTemplate(zkClient);
    }
}
