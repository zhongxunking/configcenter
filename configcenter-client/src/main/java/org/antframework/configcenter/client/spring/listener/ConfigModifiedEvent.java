/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:32 创建
 */
package org.antframework.configcenter.client.spring.listener;

import org.antframework.configcenter.client.core.ModifiedProperty;

import java.util.List;

/**
 * 配置被修改事件
 */
public class ConfigModifiedEvent {
    // 被修改的属性名前缀
    private String propertyNamePrefix;
    // 被修改的属性
    private List<ModifiedProperty> modifiedProperties;

    public ConfigModifiedEvent(String propertyNamePrefix, List<ModifiedProperty> modifiedProperties) {
        this.propertyNamePrefix = propertyNamePrefix;
        this.modifiedProperties = modifiedProperties;
    }

    public String getPropertyNamePrefix() {
        return propertyNamePrefix;
    }

    public List<ModifiedProperty> getModifiedProperties() {
        return modifiedProperties;
    }
}
