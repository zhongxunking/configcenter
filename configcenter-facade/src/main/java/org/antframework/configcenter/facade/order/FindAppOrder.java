/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:59 创建
 */
package org.antframework.configcenter.facade.order;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 */
public class FindAppOrder {

    @NotBlank
    private String appCode;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
