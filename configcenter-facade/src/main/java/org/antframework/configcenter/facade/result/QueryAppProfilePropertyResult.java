/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:04 创建
 */
package org.antframework.configcenter.facade.result;

import java.util.Map;

/**
 *
 */
public class QueryAppProfilePropertyResult {

    private Map<String, String> properties;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
