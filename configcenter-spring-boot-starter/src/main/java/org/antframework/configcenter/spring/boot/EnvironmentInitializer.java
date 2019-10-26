/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-03 10:10 创建
 */
package org.antframework.configcenter.spring.boot;

import org.antframework.configcenter.client.Config;
import org.antframework.configcenter.spring.ConfigsContexts;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * environment初始化器（将configcenter配置加入到environment）
 * <p>
 * 先初始化日志，再初始化configcenter配置。原因：
 * 1、先初始化日志的优点：初始化configcenter配置报错时，能打印日志；缺点：在configcenter中的日志相关的部分配置不会生效。
 * 2、先初始化configcenter配置的优点：在configcenter中的日志相关配置会生效；缺点：初始化configcenter配置报错时，无法打印日志。
 * 总结：一般日志需要进行动态化的配置比较少（比如：日志格式、日志文件路径等），所以设置为先初始化日志再初始化configcenter配置。
 */
@Order(EnvironmentInitializer.ORDER)
public class EnvironmentInitializer implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    /**
     * 优先级
     */
    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 30;

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        // 创建配置资源
        PropertySource propertySource = new ConfigcenterPropertySource(ConfigsContexts.getConfig(ConfigcenterProperties.INSTANCE.getRequiredAppId()));
        // 将配置资源添加到environment中
        MutablePropertySources propertySources = event.getEnvironment().getPropertySources();
        if (ConfigcenterProperties.INSTANCE.getPriorTo() == null) {
            propertySources.addLast(propertySource);
        } else {
            propertySources.addBefore(ConfigcenterProperties.INSTANCE.getPriorTo(), propertySource);
        }
    }

    /**
     * configcenter配置资源
     */
    public static class ConfigcenterPropertySource extends EnumerablePropertySource<Config> {
        /**
         * 配置资源名称
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
