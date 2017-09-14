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
 * 配置监听器
 */
public interface ConfigListener {

    /**
     * 当配置中属性被修改时触发本方法
     *
     * @param modifiedProperties 被修改的属性
     */
    void propertiesModified(List<ModifiedProperty> modifiedProperties);

}
