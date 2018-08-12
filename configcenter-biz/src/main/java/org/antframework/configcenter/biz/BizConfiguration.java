/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:42 创建
 */
package org.antframework.configcenter.biz;

import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.facade.vo.ZkConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * biz层配置
 */
@Configuration
@EnableConfigurationProperties(ConfigcenterProperties.class)
public class BizConfiguration {
    @Autowired
    private ConfigcenterProperties properties;

    // zookeeper操作类
    @Bean
    public ZkTemplate zkTemplate() {
        return ZkTemplate.create(properties.getZkUrls().toArray(new String[0]), ZkConstant.ZK_CONFIG_NAMESPACE);
    }
}
