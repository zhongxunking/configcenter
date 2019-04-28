/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 14:09 创建
 */
package org.antframework.configcenter.client;

import org.antframework.configcenter.client.core.ChangedProperty;

import java.util.List;

/**
 * 配置监听器
 */
@FunctionalInterface
public interface ConfigListener {
    /**
     * 获取优先级（值越小优先级越高）
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 当配置被修改时调用本方法
     *
     * @param changedProperties 被修改的配置
     */
    void onChange(List<ChangedProperty> changedProperties);
}
