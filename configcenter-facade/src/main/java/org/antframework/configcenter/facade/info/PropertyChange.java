/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-07 21:31 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.Property;

import java.util.HashSet;
import java.util.Set;

/**
 * 配置变动
 */
@Getter
public class PropertyChange extends AbstractInfo {
    // 添加或修改的配置
    private final Set<Property> addedOrModifiedProperties = new HashSet<>();
    // 删除的配置key
    private final Set<String> deletedKeys = new HashSet<>();

    public void addAddedOrModifiedProperty(Property property) {
        addedOrModifiedProperties.add(property);
    }

    public void addDeletedKey(String key) {
        deletedKeys.add(key);
    }
}