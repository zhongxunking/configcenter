/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-04 15:35 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.facade.AbstractInfo;

import java.util.List;

/**
 * 应用发布
 */
@AllArgsConstructor
@Getter
public class AppRelease extends AbstractInfo {
    // 应用
    private final AppInfo app;
    // 由近及远继承的环境中的发布
    private final List<ReleaseInfo> inheritedProfileReleases;
}
