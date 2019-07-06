/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-02-20 21:55 创建
 */
package org.antframework.configcenter.web.common;

import lombok.Getter;
import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.facade.vo.Property;

import java.io.Serializable;
import java.util.HashSet;
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
     * 比较配置
     *
     * @param lefts  待比较的配置
     * @param rights 待比较的配置
     * @return 差异
     */
    public static Difference compare(Set<Property> lefts, Set<Property> rights) {
        Map<String, Property> leftsMap = lefts.stream().collect(Collectors.toMap(Property::getKey, Function.identity()));
        Map<String, Property> rightsMap = rights.stream().collect(Collectors.toMap(Property::getKey, Function.identity()));

        Difference difference = new Difference();
        for (Property left : lefts) {
            Property right = rightsMap.get(left.getKey());
            if (right == null) {
                difference.addAddedKeys(left.getKey());
            } else {
                if (!Objects.equals(right.getValue(), left.getValue())) {
                    difference.addModifiedValueKey(left.getKey());
                }
                if (right.getScope() != left.getScope()) {
                    difference.addModifiedScopeKey(left.getKey());
                }
            }
        }
        for (Property right : rights) {
            if (!leftsMap.containsKey(right.getKey())) {
                difference.addRemovedKeys(right.getKey());
            }
        }

        return difference;
    }

    /**
     * 差异
     */
    @Getter
    public static class Difference implements Serializable {
        // 新增配置的key
        private Set<String> addedKeys = new HashSet<>();
        // 被修改的配置value的key
        private Set<String> modifiedValueKeys = new HashSet<>();
        // 被修改的scope的key
        private Set<String> modifiedScopeKeys = new HashSet<>();
        // 被删除配置的key
        private Set<String> removedKeys = new HashSet<>();

        public void addAddedKeys(String key) {
            addedKeys.add(key);
        }

        public void addModifiedValueKey(String key) {
            modifiedValueKeys.add(key);
        }

        public void addModifiedScopeKey(String key) {
            modifiedScopeKeys.add(key);
        }

        public void addRemovedKeys(String key) {
            removedKeys.add(key);
        }

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }
}
