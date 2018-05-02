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
public interface ConfigListener {

    /**
     * 当配置被修改时调用本方法
     *
     * @param changedProperties 被修改的属性
     */
    void onChange(List<ChangedProperty> changedProperties);
}
