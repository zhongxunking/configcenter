/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:23 创建
 */
package org.antframework.configcenter.facade.order.manage;

import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.jpa.query.annotation.operator.QueryLike;

/**
 * 查询环境order
 */
public class QueryProfileOrder extends AbstractQueryOrder {
    // 环境编码
    @QueryLike
    private String profileCode;

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }
}
