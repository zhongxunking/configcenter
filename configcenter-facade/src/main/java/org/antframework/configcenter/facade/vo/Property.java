/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:18 创建
 */
package org.antframework.configcenter.facade.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.tostring.ToString;
import org.antframework.common.util.tostring.format.Mask;

import java.io.Serializable;

/**
 * 配置项
 */
@AllArgsConstructor
@Getter
public final class Property implements Serializable {
    // key
    private final String key;
    // value
    @Mask(allMask = true)
    private final String value;
    // 作用域
    private final Scope scope;

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
