/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 22:16 创建
 */
package org.antframework.configcenter.spring.listener;

import org.antframework.configcenter.client.ConfigListener;
import org.antframework.configcenter.client.core.ChangedProperty;
import org.antframework.configcenter.spring.listener.annotation.ConfigChangedEvent;
import org.bekit.event.EventPublisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的配置监听器（将配置变更消息通知到@ConfigListener监听器）
 */
public class DefaultConfigListener implements ConfigListener {
    // key的分隔符
    private static final char KEY_SEPARATOR = '.';

    // 应用id
    private final String appId;
    // 事件发布器
    private final EventPublisher eventPublisher;

    public DefaultConfigListener(String appId, EventPublisher eventPublisher) {
        this.appId = appId;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onChange(List<ChangedProperty> changedProperties) {
        dispatch(null, changedProperties);
    }

    // 将被修改的配置按照key前缀进行递归分派
    private void dispatch(String prefixKey, List<ChangedProperty> cps) {
        Map<String, List<ChangedProperty>> dispatchedCps = new HashMap<>();
        // 根据配置key前缀进行分拣
        for (ChangedProperty cp : cps) {
            if (cp.getKey() == null) {
                continue;
            }
            String prefix = getPrefix(cp.getKey());
            ChangedProperty nextCp = new ChangedProperty(cp.getType(), getSuffix(cp.getKey()), cp.getOldValue(), cp.getNewValue());
            List<ChangedProperty> nextCps = dispatchedCps.computeIfAbsent(prefix, key -> new ArrayList<>());
            nextCps.add(nextCp);
        }
        // 将分拣过的配置通过递归继续分拣
        for (String prefix : dispatchedCps.keySet()) {
            String nextPrefixKey = prefixKey == null ? prefix : prefixKey + KEY_SEPARATOR + prefix;
            dispatch(nextPrefixKey, dispatchedCps.get(prefix));
        }
        // 发送事件
        eventPublisher.publish(new ConfigChangedEvent(appId, prefixKey, cps));
    }

    // 获取前缀（aa.bb.cc返回aa）
    private String getPrefix(String key) {
        int index = key.indexOf(KEY_SEPARATOR);
        if (index < 0) {
            return key;
        } else {
            return key.substring(0, index);
        }
    }

    // 获取后缀（aa.bb.cc返回bb.cc）
    private String getSuffix(String key) {
        int index = key.indexOf(KEY_SEPARATOR);
        if (index < 0) {
            return null;
        } else {
            return key.substring(index + 1);
        }
    }
}
