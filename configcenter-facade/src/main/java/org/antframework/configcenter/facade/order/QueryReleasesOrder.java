/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-09 17:50 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryLike;

/**
 * 查询发布order
 */
@Getter
@Setter
public class QueryReleasesOrder extends AbstractQueryOrder {
    // 应用id
    @QueryEQ
    private String appId;
    // 环境id
    @QueryEQ
    private String profileId;
    // 版本
    @QueryEQ
    private Long version;
    // 备注
    @QueryLike
    private String memo;
    // 父版本
    @QueryEQ
    private Long parentVersion;
}
