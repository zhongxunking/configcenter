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
 *
 */
public class ListenersHandler {
    private List<PropertiesListener> listeners = new CopyOnWriteArrayList<>();

    public void register(PropertiesListener listener) {
        listeners.add(listener);
    }

    public void remove(PropertiesListener listener) {
        listeners.remove(listener);
    }

    public void propertiesModified(List<ModifiedProperty> modifiedProperties) {
        if (modifiedProperties == null || modifiedProperties.size() <= 0) {
            return;
        }
        for (PropertiesListener listener : listeners) {
            listener.propertiesModified(modifiedProperties);
        }
    }
}
