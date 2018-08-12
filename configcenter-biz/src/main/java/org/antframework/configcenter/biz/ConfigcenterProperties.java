/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-12 19:38 创建
 */
package org.antframework.configcenter.biz;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

/**
 * 配置中心属性
 */
@ConfigurationProperties("configcenter")
@Validated
public class ConfigcenterProperties {
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
