/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:42 创建
 */
package org.antframework.configcenter.biz;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.facade.vo.ZkConstant;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * biz层配置
 */
@Configuration
public class BizConfiguration {
    // 属性
    private ConfigcenterProperties properties = Contexts.buildProperties(ConfigcenterProperties.class);

    // zookeeper操作类
    @Bean
    public ZkTemplate zkTemplate() {
        return ZkTemplate.create(properties.getZkUrls().toArray(new String[0]), ZkConstant.ZK_CONFIG_NAMESPACE);
    }

    /**
     * 获取配置中心的配置
     */
    public ConfigcenterProperties getProperties() {
        return properties;
    }

    /**
     * 配置中心属性
     */
    @ConfigurationProperties(ConfigcenterProperties.PREFIX)
    public static class ConfigcenterProperties {
        /**
         * 属性前缀
         */
        public static final String PREFIX = "configcenter";

        /**
         * 必填：配置中心使用的zookeeper地址，存在多个zookeeper的话以“,”分隔（比如：192.168.0.1:2181,192.168.0.2:2181）
         */
        @NotEmpty
        private Set<String> zkUrls;

        public Set<String> getZkUrls() {
            return zkUrls;
        }

        public void setZkUrls(Set<String> zkUrls) {
            this.zkUrls = zkUrls;
        }
    }
}
