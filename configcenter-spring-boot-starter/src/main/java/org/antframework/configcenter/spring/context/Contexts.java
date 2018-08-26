/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:40 创建
 */
package org.antframework.configcenter.spring.context;

import org.antframework.configcenter.spring.boot.ConfigcenterProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * 上下文持有器
 */
public final class Contexts {
    // spring环境
    private static ConfigurableEnvironment environment;
    // spring容器
    private static ConfigurableApplicationContext applicationContext;

    /**
     * 设置spring环境
     */
    public static void setEnvironment(ConfigurableEnvironment environment) {
        Contexts.environment = environment;
    }

    /**
     * 设置spring容器
     */
    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        Contexts.applicationContext = applicationContext;
    }

    /**
     * 获取Environment
     */
    public static Environment getEnvironment() {
        if (getApplicationContext() == null) {
            return environment;
        } else {
            return getApplicationContext().getEnvironment();
        }
    }

    /**
     * 获取ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取应用id
     */
    public static String getAppId() {
        return getEnvironment().resolveRequiredPlaceholders(ConfigcenterProperties.APP_ID_PATTERN);
    }

    /**
     * 获取当前环境
     */
    public static String getProfile() {
        String[] profiles = getEnvironment().getActiveProfiles();
        if (profiles.length != 1) {
            throw new IllegalStateException(String.format("当前环境[%s]必须设置，且必须为一个", AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME));
        }
        return profiles[0];
    }

    /**
     * 根据environment构建属性对象
     *
     * @param targetClass 目标类型
     * @return 属性对象
     */
    public static <T> T buildProperties(Class<T> targetClass) {
        PropertiesBinder binder = new PropertiesBinder(((ConfigurableEnvironment) getEnvironment()).getPropertySources());
        return binder.build(targetClass);
    }
}
