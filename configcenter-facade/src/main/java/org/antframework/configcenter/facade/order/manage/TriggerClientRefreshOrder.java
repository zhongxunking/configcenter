/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 15:59 创建
 */
package org.antframework.configcenter.facade.order.manage;

import org.antframework.common.util.facade.AbstractOrder;

/**
 * 触发客户端刷新配置order
 */
public class TriggerClientRefreshOrder extends AbstractOrder {
    // 应用编码（null表示刷新所有应用）
    private String appCode;
    // 环境编码（null表示刷新所有环境）
    private String profileCode;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }
}
