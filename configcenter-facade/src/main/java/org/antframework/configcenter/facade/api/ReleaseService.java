/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 20:55 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.*;

/**
 * 发布服务
 */
public interface ReleaseService {
    /**
     * 新增发布
     */
    AddReleaseResult addRelease(AddReleaseOrder order);

    /**
     * 删除发布
     */
    DeleteReleaseResult deleteRelease(DeleteReleaseOrder order);

    /**
     * 回滚发布
     */
    EmptyResult revertRelease(RevertReleaseOrder order);

    /**
     * 查找当前发布
     */
    FindCurrentReleaseResult findCurrentRelease(FindCurrentReleaseOrder order);

    /**
     * 查找发布
     */
    FindReleaseResult findRelease(FindReleaseOrder order);

    /**
     * 查询发布
     */
    QueryReleasesResult queryReleases(QueryReleasesOrder order);
}
