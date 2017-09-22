/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-22 18:12 创建
 */
package org.antframework.configcenter.web.manager.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 删除所有管理员与指定应用的关联order
 */
public class DeleteManagerAppByAppOrder extends AbstractOrder {
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
