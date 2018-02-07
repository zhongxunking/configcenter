/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:03 创建
 */
package org.antframework.configcenter.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 查找应用的所有属性key-order
 */
public class FindAppPropertyKeyOrder extends AbstractOrder {
    // 应用编码
    @NotBlank
    private String appCode;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
