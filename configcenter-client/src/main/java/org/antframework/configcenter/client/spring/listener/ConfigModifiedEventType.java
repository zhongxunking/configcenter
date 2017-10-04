/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:41 创建
 */
package org.antframework.configcenter.client.spring.listener;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 配置被修改事件类型
 */
public class ConfigModifiedEventType {
    // 配置上下文名称
    private String configContextName;
    // 被修改的属性名前缀
    private String propertyNamePrefix;

    public ConfigModifiedEventType(String configContextName, String propertyNamePrefix) {
        this.configContextName = configContextName;
        this.propertyNamePrefix = propertyNamePrefix;
    }

    @Override
    public int hashCode() {
        return Objects.hash(configContextName, propertyNamePrefix);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConfigModifiedEventType)) {
            return false;
        }
        ConfigModifiedEventType other = (ConfigModifiedEventType) obj;
        return StringUtils.equals(configContextName, other.configContextName)
                && StringUtils.equals(propertyNamePrefix, other.propertyNamePrefix);
    }
}
