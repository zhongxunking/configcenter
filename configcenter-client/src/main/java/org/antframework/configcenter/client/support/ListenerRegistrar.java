/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 15:17 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.client.ConfigListener;
import org.antframework.configcenter.client.core.ChangedProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 监听器注册器
 */
public class ListenerRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(ListenerRegistrar.class);

    // 监听器
    private List<ConfigListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * 注册监听器
     */
    public synchronized void register(ConfigListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 注销监听器
     */
    public void unregister(ConfigListener listener) {
        listeners.remove(listener);
    }

    // 配置被修改触发监听器
    synchronized void onChange(List<ChangedProperty> changedProperties) {
        if (changedProperties == null || changedProperties.size() <= 0) {
            return;
        }
        logger.info("配置变更：{}", ToString.toString(changedProperties));
        for (ConfigListener listener : listeners) {
            try {
                listener.onChange(changedProperties);
            } catch (Throwable e) {
                logger.error("配置变更调用配置监听器出错：", e);
            }
        }
    }
}
