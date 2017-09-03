/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:56 创建
 */
package org.antframework.configcenter.facade.order.manage;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 删除属性key-order
 */
public class DeletePropertyKeyOrder extends AbstractOrder {
    // 应用编码
    @NotBlank
    private String appCode;
    // key
    @NotBlank
    private String key;

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
