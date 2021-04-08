/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-09 23:23 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * 配置差异
 */
@Getter
public class PropertyDifference extends AbstractInfo {
    // 新增配置的key
    private final Set<String> addedKeys = new HashSet<>();
    // 被修改的配置value的key
    private final Set<String> modifiedValueKeys = new HashSet<>();
    // 被修改的scope的key
    private final Set<String> modifiedScopeKeys = new HashSet<>();
    // 被删除配置的key
    private final Set<String> deletedKeys = new HashSet<>();

    public void addAddedKeys(String key) {
        addedKeys.add(key);
    }

    public void addModifiedValueKey(String key) {
        modifiedValueKeys.add(key);
    }

    public void addModifiedScopeKey(String key) {
        modifiedScopeKeys.add(key);
    }

    public void addDeletedKeys(String key) {
        deletedKeys.add(key);
    }
}