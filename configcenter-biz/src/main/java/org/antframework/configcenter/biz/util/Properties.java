/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-02-20 21:55 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.configcenter.facade.info.PropertyDifference;
import org.antframework.configcenter.facade.vo.Property;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 配置工具类
 */
public final class Properties {
    /**
     * 比较配置集
     *
     * @param left  待比较的配置
     * @param right 待比较的配置
     * @return 配置差异
     */
    public static PropertyDifference compare(Set<Property> left, Set<Property> right) {
        Map<String, Property> leftMap = left.stream().collect(Collectors.toMap(Property::getKey, Function.identity()));
        Map<String, Property> rightMap = right.stream().collect(Collectors.toMap(Property::getKey, Function.identity()));

        PropertyDifference difference = new PropertyDifference();
        for (Property leftProperty : left) {
            Property rightProperty = rightMap.get(leftProperty.getKey());
            if (rightProperty == null) {
                difference.addAddedKeys(leftProperty.getKey());
            } else {
                if (!Objects.equals(rightProperty.getValue(), leftProperty.getValue())) {
                    difference.addModifiedValueKey(leftProperty.getKey());
                }
                if (rightProperty.getScope() != leftProperty.getScope()) {
                    difference.addModifiedScopeKey(leftProperty.getKey());
                }
            }
        }
        for (Property rightProperty : right) {
            if (!leftMap.containsKey(rightProperty.getKey())) {
                difference.addDeletedKeys(rightProperty.getKey());
            }
        }

        return difference;
    }
}
