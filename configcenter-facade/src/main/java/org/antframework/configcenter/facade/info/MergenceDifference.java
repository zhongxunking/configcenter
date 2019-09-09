/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-09 23:35 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.Property;

import java.util.HashSet;
import java.util.Set;

/**
 * 需合并的配置集差异
 */
@Getter
public class MergenceDifference extends AbstractInfo {
    // 需添加或修改的配置
    private final Set<Property> addOrModifiedProperties = new HashSet<>();
    // 需删除的配置key
    private final Set<String> removedPropertyKeys = new HashSet<>();

    public void addAddOrModifiedProperty(Property addOrModifiedProperty) {
        addOrModifiedProperties.add(addOrModifiedProperty);
    }

    public void addRemovedPropertyKey(String removedPropertyKey) {
        removedPropertyKeys.add(removedPropertyKey);
    }
}
