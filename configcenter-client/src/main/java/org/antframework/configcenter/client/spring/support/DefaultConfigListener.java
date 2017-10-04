/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 22:16 创建
 */
package org.antframework.configcenter.client.spring.support;

import org.antframework.configcenter.client.ConfigListener;
import org.antframework.configcenter.client.core.ModifiedProperty;
import org.antframework.configcenter.client.spring.listener.ConfigListenerType;
import org.antframework.configcenter.client.spring.listener.ConfigModifiedEvent;
import org.bekit.event.EventPublisher;
import org.bekit.event.bus.EventBusHolder;
import org.bekit.event.publisher.DefaultEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的配置监听器
 */
public class DefaultConfigListener implements ConfigListener {
    // 配置上下文名称
    private String configContextName;
    // 事件发布器
    private EventPublisher eventPublisher;

    public DefaultConfigListener() {
        this("");
    }

    public DefaultConfigListener(String configContextName) {
        this.configContextName = configContextName;
    }

    // 初始化
    @Autowired
    public void init(EventBusHolder eventBusHolder) {
        eventPublisher = new DefaultEventPublisher(eventBusHolder.getEventBus(ConfigListenerType.class));
    }

    @Override
    public void configModified(List<ModifiedProperty> modifiedProperties) {
        publish("", modifiedProperties);
    }

    private void publish(String prefixKey, List<ModifiedProperty> modifiedProperties) {
        Map<String, List<ModifiedProperty>> map = new HashMap<>();
        for (ModifiedProperty modifiedProperty : modifiedProperties) {
            if (modifiedProperty.getKey() == null) {
                continue;
            }
            String prefix = getPrefix(modifiedProperty.getKey());
            ModifiedProperty sub = new ModifiedProperty(modifiedProperty.getType(), getSubPropertyKey(modifiedProperty.getKey()), modifiedProperty.getOldValue(), modifiedProperty.getNewValue());
            List<ModifiedProperty> subs = map.get(prefix);
            if (subs == null) {
                subs = new ArrayList<>();
                map.put(prefix, subs);
            }
            subs.add(sub);
        }
        for (String key : map.keySet()) {
            publish(prefixKey + '.' + key, map.get(key));
        }
        eventPublisher.publish(new ConfigModifiedEvent(prefixKey, modifiedProperties));
    }

    private String getPrefix(String propertyKey) {
        int index = propertyKey.indexOf('.');
        if (index < 0) {
            return propertyKey;
        } else {
            return propertyKey.substring(0, index);
        }
    }

    private String getSubPropertyKey(String propertyKey) {
        int index = propertyKey.indexOf('.');
        if (index < 0) {
            return null;
        } else {
            return propertyKey.substring(index + 1);
        }
    }
}
