/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:32 创建
 */
package org.antframework.configcenter.spring.listener.annotation;

import org.antframework.configcenter.client.core.ChangedProperty;

import java.util.List;

/**
 * 配置被修改事件
 */
public final class ConfigChangedEvent {
    // 应用id
    private final String appId;
    // 被修改的配置key前缀
    private final String prefix;
    // 被修改的配置
    private final List<ChangedProperty> changedProperties;

    public ConfigChangedEvent(String appId, String prefix, List<ChangedProperty> changedProperties) {
        this.appId = appId;
        this.prefix = prefix;
        this.changedProperties = changedProperties;
    }

    public String getAppId() {
        return appId;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<ChangedProperty> getChangedProperties() {
        return changedProperties;
    }
}
