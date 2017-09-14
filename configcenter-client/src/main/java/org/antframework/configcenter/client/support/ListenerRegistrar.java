/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 15:17 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.configcenter.client.ConfigListener;
import org.antframework.configcenter.client.core.ModifiedProperty;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 监听器注册器
 */
public class ListenerRegistrar {
    // 监听器
    private List<ConfigListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * 注册监听器
     */
    public void register(ConfigListener listener) {
        listeners.add(listener);
    }

    /**
     * 删除监听器
     */
    public void remove(ConfigListener listener) {
        listeners.remove(listener);
    }

    /**
     * 配置被修改触发监听器
     *
     * @param modifiedProperties 被修改的属性
     */
    public void propertiesModified(List<ModifiedProperty> modifiedProperties) {
        if (modifiedProperties == null || modifiedProperties.size() <= 0) {
            return;
        }
        for (ConfigListener listener : listeners) {
            listener.propertiesModified(modifiedProperties);
        }
    }
}
