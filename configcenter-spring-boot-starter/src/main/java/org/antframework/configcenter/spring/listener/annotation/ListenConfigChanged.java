/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:59 创建
 */
package org.antframework.configcenter.spring.listener.annotation;

import org.bekit.event.annotation.listener.Listen;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 监听配置被修改
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Listen(resolver = ListenConfigChangedResolver.class)
public @interface ListenConfigChanged {
    /**
     * 被监听的配置key前缀
     */
    String prefix();

    /**
     * 是否按照优先级升序
     */
    @AliasFor(annotation = Listen.class, attribute = "priorityAsc")
    boolean priorityAsc() default true;
}
