/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:32 创建
 */
package org.antframework.configcenter.spring.listener.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.configcenter.client.core.ChangedProperty;

import java.util.List;

/**
 * 配置被修改事件
 */
@AllArgsConstructor
@Getter
public final class ConfigChangedEvent {
    // 应用id
    private final String appId;
    // 被修改的配置key前缀
    private final String prefix;
    // 被修改的配置
    private final List<ChangedProperty> changedProperties;
}
