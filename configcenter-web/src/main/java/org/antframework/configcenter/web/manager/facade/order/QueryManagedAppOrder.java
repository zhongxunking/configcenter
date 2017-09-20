/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 12:37 创建
 */
package org.antframework.configcenter.web.manager.facade.order;

import org.antframework.common.util.facade.AbstractQueryOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 查询被管理员管理的应用order
 */
public class QueryManagedAppOrder extends AbstractQueryOrder {
    // 用户名
    @NotBlank
    private String username;
    // 应用编码
    private String appCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
