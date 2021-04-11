/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-04 14:44 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.AppRelease;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找继承的应用发布-result
 */
@Getter
public class FindInheritedAppReleasesResult extends AbstractResult {
    // 由近及远继承的应用发布
    private final List<AppRelease> inheritedAppReleases = new ArrayList<>();

    public void addInheritedAppRelease(AppRelease appRelease) {
        inheritedAppReleases.add(appRelease);
    }
}
