/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:47 创建
 */
package org.antframework.configcenter.client;

/**
 *
 */
public interface ConfigProperties {

    String getProperty(String key);

    String[] getPropertyKeys();

    boolean containKey(String key);
}
