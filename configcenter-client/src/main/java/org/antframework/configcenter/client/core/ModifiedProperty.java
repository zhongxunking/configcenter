/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:54 创建
 */
package org.antframework.configcenter.client.core;

/**
 * 被修改的属性
 */
public class ModifiedProperty {
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
        REMOVE
    }
}
