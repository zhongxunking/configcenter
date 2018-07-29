/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:47 创建
 */
package org.antframework.configcenter.facade.info;

import org.antframework.common.util.facade.AbstractInfo;

/**
 * 应用info
 */
public class AppInfo extends AbstractInfo {
    // 应用id
    private String appId;
    // 应用名
    private String appName;
    // 父应用id（null表示无父应用）
    private String parent;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
