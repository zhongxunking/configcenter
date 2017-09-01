/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:03 创建
 */
package org.antframework.configcenter.facade.order;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 */
public class QueryAppProfilePropertyOrder {

    @NotBlank
    private String appCode;

    @NotBlank
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
