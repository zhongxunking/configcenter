/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:49 创建
 */
package org.antframework.configcenter.client.core;

import org.antframework.configcenter.client.ConfigProperties;

import java.util.List;
import java.util.Map;

/**
 * 可配置的配置属性
 */
public interface ConfigurableConfigProperties extends ConfigProperties {

    /**
     * 替换全部属性
     *
     * @param newProperties 新属性
     * @return 被修改的属性
     */
    List<ChangedProperty> replaceProperties(Map<String, String> newProperties);
}
