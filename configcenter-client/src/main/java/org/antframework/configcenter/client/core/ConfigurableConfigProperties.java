/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:49 创建
 */
package org.antframework.configcenter.client.core;

import java.util.List;
import java.util.Map;

/**
 * 可配置的配置集
 */
public interface ConfigurableConfigProperties extends ConfigProperties {

    /**
     * 替换全部配置项
     *
     * @param newProperties 新配置项
     * @return 被修改的配置项
     */
    List<ChangedProperty> replaceProperties(Map<String, String> newProperties);
}
