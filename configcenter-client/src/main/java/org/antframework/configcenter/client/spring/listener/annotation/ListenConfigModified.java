/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:59 创建
 */
package org.antframework.configcenter.client.spring.listener.annotation;

import org.antframework.configcenter.client.spring.listener.ListenConfigModifiedResolver;
import org.bekit.event.annotation.listener.Listen;

import java.lang.annotation.*;

/**
 * 监听配置被修改
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Listen(resolver = ListenConfigModifiedResolver.class)
public @interface ListenConfigModified {

    /**
     * 被监听的属性名前缀
     */
    String prefix();
}
