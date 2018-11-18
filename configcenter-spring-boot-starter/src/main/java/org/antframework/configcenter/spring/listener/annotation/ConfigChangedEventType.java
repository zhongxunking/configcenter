/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:41 创建
 */
package org.antframework.configcenter.spring.listener.annotation;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 配置被修改事件类型
 */
public class ConfigChangedEventType {
    // 应用id
    private String appId;
    // 被修改的配置key前缀
    private String prefix;

    public ConfigChangedEventType(String appId, String prefix) {
        this.appId = appId;
        this.prefix = prefix;
    }

    public String getAppId() {
        return appId;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, prefix);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConfigChangedEventType)) {
            return false;
        }
        ConfigChangedEventType other = (ConfigChangedEventType) obj;
        return StringUtils.equals(appId, other.appId)
                && StringUtils.equals(prefix, other.prefix);
    }
}
