/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-22 23:03 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用继承的所有应用result
 */
@Getter
public class FindInheritedAppsResult extends AbstractResult {
    // 由近及远继承的所用应用（该应用本身在第一位）
    private List<AppInfo> inheritedApps = new ArrayList<>();

    public void addInheritedApp(AppInfo inheritedApp) {
        inheritedApps.add(inheritedApp);
    }
}
