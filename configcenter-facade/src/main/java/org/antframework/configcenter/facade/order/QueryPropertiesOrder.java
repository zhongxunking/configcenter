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
 * 查询应用在特定环境中的配置order
 */
public class QueryPropertiesOrder extends AbstractOrder {
    // 应用编码
    @NotBlank
    private String appCode;
    // 环境编码
    @NotBlank
    private String profileCode;
    // 是否只查询公用属性
    private boolean onlyCommon;

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

    public boolean isOnlyCommon() {
        return onlyCommon;
    }

    public void setOnlyCommon(boolean onlyCommon) {
        this.onlyCommon = onlyCommon;
    }
}
