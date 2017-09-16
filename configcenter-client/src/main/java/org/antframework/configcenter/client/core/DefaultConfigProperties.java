/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:56 创建
 */
package org.antframework.configcenter.client.core;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置属性默认实现
 */
public class DefaultConfigProperties implements ConfigurableConfigProperties {
    // 属性value为null时的占位符
    private static final String NULL_VALUE = DefaultConfigProperties.class.getName() + "#NULL_VALUE";

    /**
     * 转换为可存储的value
     *
     * @param rawValue 原始value
     * @return 可存储的value
     */
    public static String toSavableValue(String rawValue) {
        return rawValue == null ? NULL_VALUE : rawValue;
    }

    /**
     * 转换为原始value
     *
     * @param savableValue 可存储的value
     * @return 原始value
     */
    public static String toRawValue(String savableValue) {
        return StringUtils.equals(savableValue, NULL_VALUE) ? null : savableValue;
    }

    // 属性
    private Map<String, String> properties = new ConcurrentHashMap<>();

    @Override
    public String getProperty(String key) {
        return toRawValue(properties.get(key));
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
