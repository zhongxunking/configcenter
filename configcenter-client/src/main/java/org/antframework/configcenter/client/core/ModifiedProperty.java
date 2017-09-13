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
 *
 */
public class ModifiedProperty {

    public static List<ModifiedProperty> analyseModify(Map<String, String> oldProperties, Map<String, String> newProperties) {
        List<ModifiedProperty> modifiedProperties = new ArrayList<>();
        for (String key : oldProperties.keySet()) {
            if (!newProperties.containsKey(key)) {
                modifiedProperties.add(new ModifiedProperty(key, ModifiedProperty.ModifyType.DELETE, oldProperties.get(key), null));
            } else if (!StringUtils.equals(newProperties.get(key), oldProperties.get(key))) {
                modifiedProperties.add(new ModifiedProperty(key, ModifiedProperty.ModifyType.UPDATE, oldProperties.get(key), newProperties.get(key)));
            }
        }
        for (String key : newProperties.keySet()) {
            if (!oldProperties.containsKey(key)) {
                modifiedProperties.add(new ModifiedProperty(key, ModifiedProperty.ModifyType.ADD, null, newProperties.get(key)));
            }
        }

        return modifiedProperties;
    }

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

    private String key;
    private ModifyType type;
    private String oldValue;
    private String newValue;

    public ModifiedProperty(String key, ModifyType type, String oldValue, String newValue) {
        this.key = key;
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ModifyType getType() {
        return type;
    }

    public void setType(ModifyType type) {
        this.type = type;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public enum ModifyType {
        ADD,
        UPDATE,
        DELETE;
    }
}
