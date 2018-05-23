/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:59 创建
 */
package org.antframework.configcenter.facade.result;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.AppInfo;

/**
 * 查找应用result
 */
public class FindAppResult extends AbstractResult {
    // 应用（null表示不存在该应用）
    private AppInfo app;

    public AppInfo getApp() {
        return app;
    }

    public void setApp(AppInfo app) {
        this.app = app;
    }
}
