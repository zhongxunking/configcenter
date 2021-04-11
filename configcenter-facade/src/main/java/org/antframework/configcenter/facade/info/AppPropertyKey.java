/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-04 17:47 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.tostring.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 应用配置key
 */
@AllArgsConstructor
@Getter
public class AppPropertyKey implements Serializable {
    // 应用
    private final AppInfo app;
    // 所有配置key
    private final List<PropertyKeyInfo> propertyKeys;

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
