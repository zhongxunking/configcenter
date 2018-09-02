/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:56 创建
 */
package org.antframework.configcenter.client.core;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 配置属性默认实现
 */
public class DefaultConfigProperties implements ConfigurableConfigProperties {
    // 属性
    private volatile Map<String, String> properties = new HashMap<>();

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public String[] getPropertyKeys() {
        Set<String> keys = properties.keySet();
        return keys.toArray(new String[keys.size()]);
    }

    @Override
    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    @Override
    public synchronized List<ChangedProperty> replaceProperties(Map<String, String> newProperties) {
        List<ChangedProperty> changedProperties = analyseChanges(properties, newProperties);
        properties = new HashMap<>(newProperties);
        return changedProperties;
    }

    // 分析被修改的属性
    private static List<ChangedProperty> analyseChanges(Map<String, String> oldProperties, Map<String, String> newProperties) {
        List<ChangedProperty> changedProperties = new ArrayList<>();
        // 分析删除和修改的属性
        for (String key : oldProperties.keySet()) {
            if (!newProperties.containsKey(key)) {
                changedProperties.add(new ChangedProperty(ChangedProperty.ChangeType.REMOVE, key, oldProperties.get(key), null));
            } else if (!StringUtils.equals(newProperties.get(key), oldProperties.get(key))) {
                changedProperties.add(new ChangedProperty(ChangedProperty.ChangeType.UPDATE, key, oldProperties.get(key), newProperties.get(key)));
            }
        }
        // 分析新增的属性
        for (String key : newProperties.keySet()) {
            if (!oldProperties.containsKey(key)) {
                changedProperties.add(new ChangedProperty(ChangedProperty.ChangeType.ADD, key, null, newProperties.get(key)));
            }
        }

        return changedProperties;
    }
}
