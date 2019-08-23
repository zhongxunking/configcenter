/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 21:42 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.AddBranchOrder;
import org.antframework.configcenter.facade.order.FindBranchOrder;
import org.antframework.configcenter.facade.order.FindBranchReleaseOrder;
import org.antframework.configcenter.facade.result.FindBranchReleaseResult;
import org.antframework.configcenter.facade.result.FindBranchResult;

/**
 * 分支服务
 */
public interface BranchService {
    /**
     * 添加分支
     */
    EmptyResult addBranch(AddBranchOrder order);

    /**
     * 查找分支
     */
    FindBranchResult findBranch(FindBranchOrder order);

    /**
     * 查找分支发布
     */
    FindBranchReleaseResult findBranchRelease(FindBranchReleaseOrder order);
}
