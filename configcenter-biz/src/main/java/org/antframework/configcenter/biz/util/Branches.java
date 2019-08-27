/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:49 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.BranchService;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.order.DeleteDetachedReleasesOrder;
import org.antframework.configcenter.facade.order.FindBranchOrder;
import org.antframework.configcenter.facade.result.FindBranchResult;

/**
 * 分支操作类
 */
public final class Branches {
    // 分支服务
    private static final BranchService BRANCH_SERVICE = Contexts.getApplicationContext().getBean(BranchService.class);

    /**
     * 查找分支
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     * @return 分支（null表示无该分支）
     */
    public static BranchInfo findBranch(String appId, String profileId, String branchId) {
        FindBranchOrder order = new FindBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        FindBranchResult result = BRANCH_SERVICE.findBranch(order);
        FacadeUtils.assertSuccess(result);
        return result.getBranch();
    }

    /**
     * 删除分离的发布
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param releaseVersion 发布版本
     */
    public static void deleteDetachedReleases(String appId, String profileId, long releaseVersion) {
        DeleteDetachedReleasesOrder order = new DeleteDetachedReleasesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setReleaseVersion(releaseVersion);

        EmptyResult result = BRANCH_SERVICE.deleteDetachedReleases(order);
        FacadeUtils.assertSuccess(result);
    }
}
