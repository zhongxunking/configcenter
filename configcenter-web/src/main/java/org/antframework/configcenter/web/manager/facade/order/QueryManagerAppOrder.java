/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:48 创建
 */
package org.antframework.configcenter.web.manager.facade.order;

import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.jpa.query.annotation.operator.QueryLike;

/**
 * 查询管理员和应用关联order
 */
public class QueryManagerAppOrder extends AbstractQueryOrder {
    // 管理员编码
    @QueryLike
    private String managerCode;
    // 被管理的应用编码
    @QueryLike
    private String appCode;

    public String getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(String managerCode) {
        this.managerCode = managerCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
