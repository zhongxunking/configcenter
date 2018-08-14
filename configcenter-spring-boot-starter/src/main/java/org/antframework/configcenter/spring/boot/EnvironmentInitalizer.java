/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-03 10:10 创建
 */
package org.antframework.configcenter.spring.boot;

import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.spring.ConfigContexts;
import org.antframework.configcenter.spring.context.Contexts;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * environment初始化器（将配置中心的配置加入到environment）
 */
public class EnvironmentInitalizer implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        // 创建配置上下文属性资源
        PropertySource propertySource = new ConfigContextPropertySource(ConfigContexts.get(Contexts.getAppId()));
        // 将属性资源添加到environment中
        MutablePropertySources propertySources = event.getEnvironment().getPropertySources();
        if (ConfigcenterProperties.INSTANCE.getPriorTo() == null) {
            propertySources.addLast(propertySource);
        } else {
            propertySources.addBefore(ConfigcenterProperties.INSTANCE.getPriorTo(), propertySource);
        }
    }

    @Override
    public int getOrder() {
        if (ConfigcenterProperties.INSTANCE.isInitBeforeLogging()) {
            return LoggingApplicationListener.DEFAULT_ORDER - 1;
        } else {
            return LoggingApplicationListener.DEFAULT_ORDER + 1;
        }
    }

    /**
     * 配置上下文属性资源
     */
    public static class ConfigContextPropertySource extends EnumerablePropertySource<ConfigContext> {
        /**
         * 属性资源名称
         */
        public static final String PROPERTY_SOURCE_NAME = "configcenter";

        public ConfigContextPropertySource(ConfigContext source) {
            super(PROPERTY_SOURCE_NAME, source);
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
}
