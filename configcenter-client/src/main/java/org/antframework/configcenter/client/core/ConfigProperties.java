/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:47 创建
 */
package org.antframework.configcenter.client.core;

/**
 * 配置项集合
 */
public interface ConfigProperties {
    /**
     * 获取配置项value
     *
     * @param key 配置项key
     * @return 配置项value
     */
    String getProperty(String key);

    /**
     * 获取所有配置项key
     */
    String[] getPropertyKeys();

    /**
     * 是否包含指定配置项
     */
    boolean contains(String key);
}
