/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 21:26 创建
 */
package org.antframework.configcenter.client.spring;

import org.antframework.configcenter.client.ConfigContext;
import org.springframework.core.env.EnumerablePropertySource;

/**
 * 以配置中心作为属性资源
 */
public class ConfigcenterPropertySource extends EnumerablePropertySource<ConfigContext> {
    /**
     * 属性资源名称
     */
    public static final String PROPERTY_SOURCE_NAME = "configcenter";

    public ConfigcenterPropertySource(String name, ConfigContext source) {
        super(name, source);
    }

    @Override
    public boolean containsProperty(String name) {
        return source.getProperties().contains(name);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getProperties().getPropertyKeys();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperties().getProperty(name);
    }
}
