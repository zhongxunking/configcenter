/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-04 12:01 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.AppPropertyKey;

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
}
