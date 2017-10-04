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
    // key的分隔符
    private static final char KEY_SEPARATOR = '.';

    // 配置上下文名称
    private String configContextName;
    // 事件发布器
    private EventPublisher eventPublisher;

    public DefaultConfigListener() {
        this(org.antframework.configcenter.client.spring.listener.annotation.ConfigListener.DEFAULT_CONFIG_CONTEXT_NAME);
    }

    public DefaultConfigListener(String configContextName) {
        this.configContextName = configContextName;
    }

    /**
     * 初始化（如果是作为spring的bean，spring会自动调用本方法，不需要手动调用）
     *
     * @param eventBusHolder 事件总线持有器
     */
    @Autowired
    public void init(EventBusHolder eventBusHolder) {
        eventPublisher = new DefaultEventPublisher(eventBusHolder.getEventBus(ConfigListenerType.class));
    }

    @Override
    public void configModified(List<ModifiedProperty> modifiedProperties) {
        dispatch("", modifiedProperties);
    }

    // 将被修改的属性按照属性名前缀进行递归分派
    private void dispatch(String prefixKey, List<ModifiedProperty> modifiedProperties) {
        Map<String, List<ModifiedProperty>> dispatchedMPs = new HashMap<>();
        // 根据属性key前缀进行分拣
        for (ModifiedProperty mp : modifiedProperties) {
            if (mp.getKey() == null) {
                continue;
            }
            String prefix = getKeyPrefix(mp.getKey());
            ModifiedProperty dispatchedMp = new ModifiedProperty(mp.getType(), getSuffixKey(mp.getKey()), mp.getOldValue(), mp.getNewValue());
            List<ModifiedProperty> mps = dispatchedMPs.get(prefix);
            if (mps == null) {
                mps = new ArrayList<>();
                dispatchedMPs.put(prefix, mps);
            }
            mps.add(dispatchedMp);
        }
        // 将分拣的属性通过递归继续分拣
        for (String prefix : dispatchedMPs.keySet()) {
            dispatch(prefixKey + KEY_SEPARATOR + prefix, dispatchedMPs.get(prefix));
        }
        // 发送事件
        eventPublisher.publish(new ConfigModifiedEvent(configContextName, prefixKey, modifiedProperties));
    }

    // 获取key前缀（aa.bb.cc返回aa）
    private String getKeyPrefix(String key) {
        int index = key.indexOf(KEY_SEPARATOR);
        if (index < 0) {
            return key;
        } else {
            return key.substring(0, index);
        }
    }

    // 获取key后缀（aa.bb.cc返回bb.cc）
    private String getSuffixKey(String key) {
        int index = key.indexOf(KEY_SEPARATOR);
        if (index < 0) {
            return null;
        } else {
            return key.substring(index + 1);
        }
    }
}
