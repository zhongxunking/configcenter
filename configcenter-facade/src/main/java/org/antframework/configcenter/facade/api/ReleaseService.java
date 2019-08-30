/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 20:55 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.configcenter.facade.order.FindReleaseOrder;
import org.antframework.configcenter.facade.order.QueryReleasesOrder;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.result.QueryReleasesResult;

/**
 * 发布服务
 */
public interface ReleaseService {
    /**
     * 查找发布
     */
    FindReleaseResult findRelease(FindReleaseOrder order);

    /**
     * 查询发布
     */
    QueryReleasesResult queryReleases(QueryReleasesOrder order);
}
