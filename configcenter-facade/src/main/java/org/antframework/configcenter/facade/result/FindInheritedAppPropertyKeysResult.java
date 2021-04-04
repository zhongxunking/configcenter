/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-04 12:01 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查找继承的应用配置key-result
 */
@Getter
public class FindInheritedAppPropertyKeysResult extends AbstractResult {
    // 由近及远继承的应用配置key
    private final List<AppPropertyKey> inheritedAppPropertyKeys = new ArrayList<>();

    public void addInheritedAppPropertyKey(AppPropertyKey inheritedAppPropertyKey) {
        inheritedAppPropertyKeys.add(inheritedAppPropertyKey);
    }

    /**
     * 应用配置key
     */
    @AllArgsConstructor
    @Getter
    public static final class AppPropertyKey implements Serializable {
        // 应用
        private final AppInfo app;
        // 所有配置key
        private final List<PropertyKeyInfo> propertyKeys;

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }
}
