/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-04-24 23:36 创建
 */
package org.antframework.configcenter.client.support;

import lombok.extern.slf4j.Slf4j;
import org.antframework.configcenter.client.ConfigListener;
import org.antframework.configcenter.client.core.ChangedProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 配置监听器的管理器
 */
@Slf4j
public class ConfigListeners {
    // 监听器
    private List<ConfigListener> listeners = new ArrayList<>();

    /**
     * 添加监听器
     *
     * @param listener 需添加的监听器
     */
    public synchronized void addListener(ConfigListener listener) {
        if (!listeners.contains(listener)) {
            List<ConfigListener> nextListeners = new ArrayList<>(listeners);
            nextListeners.add(listener);
            nextListeners.sort(Comparator.comparingInt(ConfigListener::getOrder));
            listeners = nextListeners;
        }
    }

    /**
     * 删除监听器
     *
     * @param listener 需删除的监听器
     */
    public synchronized void removeListener(ConfigListener listener) {
        if (listeners.contains(listener)) {
            List<ConfigListener> nextListeners = new ArrayList<>(listeners);
            nextListeners.remove(listener);
            listeners = nextListeners;
        }
    }

    // 配置变更后通知监听器
    synchronized void onChange(List<ChangedProperty> changedProperties) {
        for (ConfigListener listener : listeners) {
            try {
                listener.onChange(changedProperties);
            } catch (Throwable e) {
                log.error("configcenter配置变更后通知配置监听器出错", e);
            }
        }
    }
}
