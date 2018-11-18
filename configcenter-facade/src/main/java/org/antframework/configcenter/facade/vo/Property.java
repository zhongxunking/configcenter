/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:18 创建
 */
package org.antframework.configcenter.facade.vo;

import org.antframework.common.util.tostring.ToString;

import java.io.Serializable;

/**
 * 配置项
 */
public final class Property implements Serializable {
    // key
    private final String key;
    // value
    private final String value;
    // 作用域
    private final Scope scope;

    public Property(String key, String value, Scope scope) {
        this.key = key;
        this.value = value;
        this.scope = scope;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
