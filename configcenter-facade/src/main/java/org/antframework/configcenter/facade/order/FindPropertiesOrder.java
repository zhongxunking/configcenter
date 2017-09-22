/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:03 创建
 */
package org.antframework.configcenter.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 查找应用在特定环境中的配置order
 */
public class FindPropertiesOrder extends AbstractOrder {
    // 应用编码
    @NotBlank
    private String appCode;
    // 环境编码
    @NotBlank
    private String profileCode;
    // 是否只查询公开的属性
    private boolean onlyOutward;

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

    public boolean isOnlyOutward() {
        return onlyOutward;
    }

    public void setOnlyOutward(boolean onlyOutward) {
        this.onlyOutward = onlyOutward;
    }
}
