/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 21:42 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.FindBranchReleaseResult;
import org.antframework.configcenter.facade.result.FindBranchResult;
import org.antframework.configcenter.facade.result.MergeBranchResult;

/**
 * 分支服务
 */
public interface BranchService {
    /**
     * 添加分支
     */
    EmptyResult addBranch(AddBranchOrder order);

    /**
     * 发布分支
     */
    EmptyResult releaseBranch(ReleaseBranchOrder order);

    /**
     * 回滚分支
     */
    EmptyResult revertBranch(RevertBranchOrder order);

    /**
     * 删除分离的发布
     */
    EmptyResult deleteDetachedReleases(DeleteDetachedReleasesOrder order);

    /**
     * 合并分支
     */
    MergeBranchResult mergeBranch(MergeBranchOrder order);

    /**
     * 删除分支
     */
    EmptyResult deleteBranch(DeleteBranchOrder order);

    /**
     * 查找分支
     */
    FindBranchResult findBranch(FindBranchOrder order);

    /**
     * 查找分支发布
     */
    FindBranchReleaseResult findBranchRelease(FindBranchReleaseOrder order);
}
