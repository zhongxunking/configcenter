/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 15:17 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.configcenter.client.PropertiesListener;
import org.antframework.configcenter.client.core.ModifiedProperty;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 监听器处理器
 */
public class ListenersHandler {
    // 监听器
    private List<PropertiesListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * 注册监听器
     */
    public void register(PropertiesListener listener) {
        listeners.add(listener);
    }

    /**
     * 删除监听器
     */
    public void remove(PropertiesListener listener) {
        listeners.remove(listener);
    }

    /**
     * 属性被修改触发监听器
     *
     * @param modifiedProperties 被修改的属性
     */
    public void propertiesModified(List<ModifiedProperty> modifiedProperties) {
        if (modifiedProperties == null || modifiedProperties.size() <= 0) {
            return;
        }
        for (PropertiesListener listener : listeners) {
            listener.propertiesModified(modifiedProperties);
        }
    }
}
