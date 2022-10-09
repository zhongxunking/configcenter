/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:54 创建
 */
package org.antframework.configcenter.client.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.tostring.ToString;
import org.antframework.common.util.tostring.mask.Mask;

import java.io.Serializable;

/**
 * 被修改的配置项
 */
@AllArgsConstructor
@Getter
public class ChangedProperty implements Serializable {
    // 修改类型
    private final ChangeType type;
    // key
    private final String key;
    // 旧值
    @Mask(secureMask = true)
    private final String oldValue;
    // 新值
    @Mask(secureMask = true)
    private final String newValue;

    @Override
    public String toString() {
        return ToString.toString(this);
    }

    /**
     * 修改类型
     */
    public enum ChangeType {
        // 新增
        ADD,
        // 更新
        UPDATE,
        // 删除
        REMOVE
    }
}
