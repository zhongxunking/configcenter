/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 16:01 创建
 */
package org.antframework.configcenter.client.spring.listener;

import org.antframework.configcenter.client.core.ModifiedProperty;
import org.antframework.configcenter.client.spring.listener.annotation.ListenConfigModified;
import org.bekit.event.extension.ListenResolver;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 监听配置被修改解决器
 */
public class ListenConfigModifiedResolver implements ListenResolver {
    private ConfigModifiedEventType eventType;

    @Override
    public void init(Method listenMethod) {
        // 校验入参
        Class[] parameterTypes = listenMethod.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new RuntimeException("监听配置被修改方法" + ClassUtils.getQualifiedMethodName(listenMethod) + "的入参必须是（List<ModifiedProperty>）");
        }
        if (parameterTypes[0] != List.class) {
            throw new RuntimeException("监听配置被修改方法" + ClassUtils.getQualifiedMethodName(listenMethod) + "的入参必须是（List<ModifiedProperty>）");
        }
        ResolvableType resolvableType = ResolvableType.forType(parameterTypes[0]);
        if (resolvableType.getGeneric(0).resolve(Object.class) != ModifiedProperty.class) {
            throw new RuntimeException("监听配置被修改方法" + ClassUtils.getQualifiedMethodName(listenMethod) + "的入参必须是（List<ModifiedProperty>）");
        }
        // 设置事件类型
        ListenConfigModified listenConfigModifiedAnnotation = AnnotatedElementUtils.findMergedAnnotation(listenMethod, ListenConfigModified.class);
        eventType = new ConfigModifiedEventType(listenConfigModifiedAnnotation.propertyNamePrefix());
    }

    @Override
    public Object getEventType() {
        return eventType;
    }

    @Override
    public Object[] resolveArgs(Object event) {
        ConfigModifiedEvent configModifiedEvent = (ConfigModifiedEvent) event;
        return new Object[]{configModifiedEvent.getModifiedProperties()};
    }
}
