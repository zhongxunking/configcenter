/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-03 10:10 创建
 */
package org.antframework.configcenter.spring.boot;

import org.antframework.configcenter.client.Config;
import org.antframework.configcenter.spring.ConfigContexts;
import org.antframework.configcenter.spring.context.Contexts;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * environment初始化器（将配置中心的配置加入到environment）
 * <p>
 * 日志初始化后再初始化配置中心的配置。原因：
 * 1、比日志先初始化的好处：在配置中心的日志相关配置会生效；坏处：初始化配置中心的配置报错时，日志打印不出来。
 * 2、比日志后初始化的好处：初始化配置中心的配置报错时，能打印日志；坏处：在配置中心的日志相关配置不会生效。
 * 总结：一般日志需要进行动态化的配置比较少（比如：日志格式、日志文件路径等），所以设置为日志初始化后再初始化配置中心的配置。
 */
@Order(LoggingApplicationListener.DEFAULT_ORDER + 1)
public class EnvironmentInitializer implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        // 创建配置中心属性资源
        PropertySource propertySource = new ConfigcenterPropertySource(ConfigContexts.getConfig(Contexts.getAppId()));
        // 将属性资源添加到environment中
        MutablePropertySources propertySources = event.getEnvironment().getPropertySources();
        if (ConfigcenterProperties.INSTANCE.getPriorTo() == null) {
            propertySources.addLast(propertySource);
        } else {
            propertySources.addBefore(ConfigcenterProperties.INSTANCE.getPriorTo(), propertySource);
        }
    }

    /**
     * 配置中心属性资源
     */
    public static class ConfigcenterPropertySource extends EnumerablePropertySource<Config> {
        /**
         * 属性资源名称
         */
        public static final String PROPERTY_SOURCE_NAME = "configcenter";

        public ConfigcenterPropertySource(Config source) {
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
