/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:57 创建
 */
package org.antframework.configcenter.facade.order.manage;

import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryLike;

/**
 * 查询属性key-order
 */
public class QueryPropertyKeyOrder extends AbstractQueryOrder {
    // 应用编码
    @QueryLike
    private String appCode;
    // 属性key
    @QueryLike
    private String key;
    // 是否是公开的
    @QueryEQ
    private Boolean outward;

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

    public Boolean getOutward() {
        return outward;
    }

    public void setOutward(Boolean outward) {
        this.outward = outward;
    }
}
