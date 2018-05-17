/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 15:59 创建
 */
package org.antframework.configcenter.facade.order;

import org.antframework.common.util.facade.AbstractOrder;

/**
 * 触发客户端刷新配置order
 */
public class TriggerClientsRefreshOrder extends AbstractOrder {
    // 应用id（null表示刷新所有应用）
    private String appId;
    // 环境id（null表示刷新所有环境）
    private String profileId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
