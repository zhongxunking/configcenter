/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:23 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryLike;

/**
 * 查询环境order
 */
@Getter
@Setter
public class QueryProfilesOrder extends AbstractQueryOrder {
    // 环境id
    @QueryLike
    private String profileId;
}
