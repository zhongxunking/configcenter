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
    // 配置上下文名称
    private String configContextName;
    // 被修改的属性名前缀
    private String prefix;
    // 被修改的属性
    private List<ModifiedProperty> modifiedProperties;

    public ConfigModifiedEvent(String configContextName, String prefix, List<ModifiedProperty> modifiedProperties) {
        this.configContextName = configContextName;
        this.prefix = prefix;
        this.modifiedProperties = modifiedProperties;
    }

    public String getConfigContextName() {
        return configContextName;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<ModifiedProperty> getModifiedProperties() {
        return modifiedProperties;
    }
}
