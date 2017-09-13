/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 14:09 创建
 */
package org.antframework.configcenter.client;

import org.antframework.configcenter.client.core.ModifiedProperty;

import java.util.List;

/**
 *
 */
public interface PropertiesListener {

    void propertiesModified(List<ModifiedProperty>)

}
