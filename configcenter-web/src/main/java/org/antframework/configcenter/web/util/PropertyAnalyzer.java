/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-01-10 20:42 创建
 */
package org.antframework.configcenter.web.util;

import lombok.Getter;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.web.common.KeyPrivileges;
import org.antframework.configcenter.web.common.Privilege;

import java.io.Serializable;
import java.util.*;

/**
 * 配置工具类
 */
public final class PropertyAnalyzer {

    /**
     * 分析被修改的配置value
     *
     * @param source 源配置
     * @param target 目标配置
     * @return 修改的配置
     */
    public static ChangedProperties analyzeChangedPropretyValues(List<Property> source, List<PropertyValueInfo> target) {
        ChangedProperties changedProperties = new ChangedProperties();
        // 分析新增和被修改的配置
        Map<String, Property> sourceMap = new HashMap<>(source.size());
        for (Property property : source) {
            sourceMap.put(property.getKey(), property);
        }
        for (PropertyValueInfo propertyValue : target) {
            Property property = sourceMap.get(propertyValue.getKey());
            if (property == null) {
                changedProperties.addAddedKeys(propertyValue.getKey());
            } else {
                if (!Objects.equals(propertyValue.getValue(), property.getValue())) {
                    changedProperties.addModifiedValueKey(propertyValue.getKey());
                }
                if (propertyValue.getScope() != property.getScope()) {
                    changedProperties.addModifiedScopeKey(propertyValue.getKey());
                }
            }
        }
        // 分析被删除的配置
        Map<String, PropertyValueInfo> targetMap = new HashMap<>(target.size());
        for (PropertyValueInfo propertyValue : target) {
            targetMap.put(propertyValue.getKey(), propertyValue);
        }
        for (Property property : source) {
            PropertyValueInfo propertyValue = targetMap.get(property.getKey());
            if (propertyValue == null) {
                changedProperties.addRemovedKeys(property.getKey());
            }
        }

        return changedProperties;
    }

    /**
     * 分析修改的配置
     *
     * @param source 源配置
     * @param target 目标配置
     * @return 修改的配置
     */
    public static ChangedProperties analyzeChangedProperties(List<Property> source, List<Property> target) {
        ChangedProperties changedProperties = new ChangedProperties();
        // 分析新增和被修改的配置
        Map<String, Property> sourceMap = new HashMap<>(source.size());
        for (Property property : source) {
            sourceMap.put(property.getKey(), property);
        }
        for (Property targetProperty : target) {
            Property sourceProperty = sourceMap.get(targetProperty.getKey());
            if (sourceProperty == null) {
                changedProperties.addAddedKeys(targetProperty.getKey());
            } else {
                if (!Objects.equals(targetProperty.getValue(), sourceProperty.getValue())) {
                    changedProperties.addModifiedValueKey(targetProperty.getKey());
                }
                if (targetProperty.getScope() != sourceProperty.getScope()) {
                    changedProperties.addModifiedScopeKey(targetProperty.getKey());
                }
            }
        }
        // 分析被删除的配置
        Map<String, Property> targetMap = new HashMap<>(target.size());
        for (Property property : target) {
            targetMap.put(property.getKey(), property);
        }
        for (Property sourceProperty : source) {
            Property targetProperty = targetMap.get(sourceProperty.getKey());
            if (targetProperty == null) {
                changedProperties.addRemovedKeys(sourceProperty.getKey());
            }
        }

        return changedProperties;
    }

    /**
     * 断言只有读写权限的配置被修改
     *
     * @param appId             应用id
     * @param changedProperties 被修改的配置
     */
    public static void onlyHaveReadWritePrivilege(String appId, ChangedProperties changedProperties) {
        List<KeyPrivileges.AppPrivilege> appPrivileges = KeyPrivileges.findInheritedPrivileges(appId);

        Set<String> keys = new HashSet<>();
        keys.addAll(changedProperties.getAddedKeys());
        keys.addAll(changedProperties.getModifiedValueKeys());
        keys.addAll(changedProperties.getModifiedScopeKeys());
        keys.addAll(changedProperties.getRemovedKeys());

        Set<String> notReadWriteKeys = new HashSet<>();
        for (String key : keys) {
            Privilege privilege = KeyPrivileges.calcPrivilege(appPrivileges, key);
            if (privilege != Privilege.READ_WRITE) {
                notReadWriteKeys.add(key);
            }
        }

        if (!notReadWriteKeys.isEmpty()) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("存在敏感配置%s被修改", ToString.toString(notReadWriteKeys)));
        }
    }

    /**
     * 被修改的配置集
     */
    @Getter
    public static class ChangedProperties implements Serializable {
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
