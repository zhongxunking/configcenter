/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:53 创建
 */
package org.antframework.configcenter.facade.order.manage;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 */
public class AddOrModifyPropertyKeyOrder {

    @NotBlank
    private String appCode;

    @NotBlank
    private String key;

    private String memo;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
