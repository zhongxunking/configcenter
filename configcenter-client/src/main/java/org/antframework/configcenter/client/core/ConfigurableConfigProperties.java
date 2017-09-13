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
 *
 */
public interface ConfigurableConfigProperties extends ConfigProperties {

    List<ModifiedProperty> replaceProperties(Map<String, String> newProperties);

}
