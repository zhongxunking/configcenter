/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:57 创建
 */
package org.antframework.configcenter.facade.order;

import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryLike;

/**
 * 查询属性key-order
 */
public class QueryPropertyKeysOrder extends AbstractQueryOrder {
    // 应用id
    @QueryLike
    private String appId;
    // 属性key
    @QueryLike
    private String key;
    // 是否是公开的
    @QueryEQ
    private Boolean outward;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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
