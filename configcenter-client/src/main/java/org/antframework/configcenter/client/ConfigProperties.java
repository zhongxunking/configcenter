/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:47 创建
 */
package org.antframework.configcenter.client;

/**
 * 配置属性
 */
public interface ConfigProperties {
    /**
     * 获取属性
     *
     * @param key 属性key
     * @return 属性value
     */
    String getProperty(String key);

    /**
     * 获取所有属性key
     */
    String[] getPropertyKeys();

    /**
     * 是否包含属性key
     */
    boolean containKey(String key);
}
