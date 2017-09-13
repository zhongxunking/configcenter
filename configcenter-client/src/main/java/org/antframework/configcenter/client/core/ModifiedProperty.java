/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:54 创建
 */
package org.antframework.configcenter.client.core;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 被修改的属性
 */
public class ModifiedProperty {

    /**
     * 分析被修改的属性
     *
     * @param oldProperties 旧属性
     * @param newProperties 新属性
     * @return 被修改的属性
     */
    public static List<ModifiedProperty> analyseModify(Map<String, String> oldProperties, Map<String, String> newProperties) {
        List<ModifiedProperty> modifiedProperties = new ArrayList<>();
        // 分析删除和修改的属性
        for (String key : oldProperties.keySet()) {
            if (!newProperties.containsKey(key)) {
                modifiedProperties.add(new ModifiedProperty(ModifiedProperty.ModifyType.DELETE, key, oldProperties.get(key), null));
            } else if (!StringUtils.equals(newProperties.get(key), oldProperties.get(key))) {
                modifiedProperties.add(new ModifiedProperty(ModifiedProperty.ModifyType.UPDATE, key, oldProperties.get(key), newProperties.get(key)));
            }
        }
        // 分析新增的属性
        for (String key : newProperties.keySet()) {
            if (!oldProperties.containsKey(key)) {
                modifiedProperties.add(new ModifiedProperty(ModifiedProperty.ModifyType.ADD, key, null, newProperties.get(key)));
            }
        }

        return modifiedProperties;
    }

    /**
     * 应用被修改的属性
     *
     * @param properties         属性
     * @param modifiedProperties 被修改的属性
     */
    public static void applyModify(Map<String, String> properties, List<ModifiedProperty> modifiedProperties) {
        for (ModifiedProperty modifiedProperty : modifiedProperties) {
            switch (modifiedProperty.getType()) {
                case ADD:
                case UPDATE:
                    properties.put(modifiedProperty.getKey(), modifiedProperty.getNewValue());
                    break;
                case DELETE:
                    properties.remove(modifiedProperty.getKey());
                    break;
                default:
                    throw new IllegalArgumentException("无法识别的修改类型");
            }
        }
    }

    // 修改类型
    private ModifyType type;
    // 属性key
    private String key;
    // 旧属性值
    private String oldValue;
    // 新属性值
    private String newValue;

    public ModifiedProperty(ModifyType type, String key, String oldValue, String newValue) {
        this.type = type;
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public ModifyType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    /**
     * 修改类型
     */
    public enum ModifyType {
        // 新增
        ADD,

        // 更新
        UPDATE,

        // 删除
        DELETE
    }
}
