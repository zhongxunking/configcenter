/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:56 创建
 */
package org.antframework.configcenter.client.core;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class DefaultConfigProperties implements ConfigurableConfigProperties {

    private Map<String, String> properties = new ConcurrentHashMap<>();

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
    public boolean containKey(String key) {
        return properties.containsKey(key);
    }

    @Override
    public List<ModifiedProperty> replaceProperties(Map<String, String> newProperties) {
        List<ModifiedProperty> modifiedProperties = ModifiedProperty.analyseModify(properties, newProperties);
        ModifiedProperty.applyModify(properties, modifiedProperties);
        return modifiedProperties;
    }
}
