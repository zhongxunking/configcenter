/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:21 创建
 */
package org.antframework.configcenter.client.spring.listener.annotation;

import org.antframework.configcenter.client.spring.listener.ConfigListenerType;
import org.bekit.event.annotation.listener.Listener;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 配置监听器
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Listener(type = ConfigListenerType.class)
public @interface ConfigListener {
    /**
     * 默认的配置上下文名称
     */
    String DEFAULT_CONFIG_CONTEXT_NAME = "default";

    /**
     * 被监听的配置上下文的名称
     */
    String configContextName() default DEFAULT_CONFIG_CONTEXT_NAME;

    /**
     * 优先级
     */
    @AliasFor(annotation = Listener.class, attribute = "priority")
    int priority() default Integer.MAX_VALUE;
}
