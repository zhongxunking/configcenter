/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:26 创建
 */
package org.antframework.configcenter.facade.order.manage;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 */
public class DeletePropertyValueOrder {

    @NotBlank
    private String profileCode;

    @NotBlank
    private String appCode;

    @NotBlank
    private String key;

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
