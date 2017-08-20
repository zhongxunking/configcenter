/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:22 创建
 */
package org.antframework.configcenter.facade.order.manage;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 */
public class DeleteProfileOrder {

    @NotBlank
    private String profileCode;

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }
}
