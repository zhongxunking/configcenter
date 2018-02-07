/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:19 创建
 */
package org.antframework.configcenter.facade.order;

import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryLike;

/**
 * 查询应用order
 */
public class QueryAppOrder extends AbstractQueryOrder {
    // 应用编码
    @QueryLike
    private String appCode;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
