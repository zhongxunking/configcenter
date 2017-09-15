/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:19 创建
 */
package org.antframework.configcenter.facade.order.manage;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 查找应用在指定环境的所有属性value-order
 */
public class FindAppProfilePropertyValueOrder extends AbstractOrder {
    // 应用编码
    @NotBlank
    private String appCode;
    // 环境编码
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
