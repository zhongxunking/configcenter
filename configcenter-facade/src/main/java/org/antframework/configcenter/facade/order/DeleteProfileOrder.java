/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:22 创建
 */
package org.antframework.configcenter.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 删除环境order
 */
public class DeleteProfileOrder extends AbstractOrder {
    // 环境编码
    @NotBlank
    private String profileCode;

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }
}
