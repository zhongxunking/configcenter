/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:04 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用的配置key-result
 */
@Getter
public class FindAppPropertyKeysResult extends AbstractResult {
    // 应用的配置key
    private final List<PropertyKeyInfo> propertyKeys = new ArrayList<>();

    public void addPropertyKey(PropertyKeyInfo propertyKey) {
        propertyKeys.add(propertyKey);
    }
}
