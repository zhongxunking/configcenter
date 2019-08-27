/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-27 23:50 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.facade.order.DeleteDetachedReleasesOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 删除分离的发布服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteDetachedReleasesService {
    // 分支dao
    private final BranchDao branchDao;

    @ServiceExecute
    public void execute(ServiceContext<DeleteDetachedReleasesOrder, EmptyResult> context) {
        DeleteDetachedReleasesOrder order = context.getOrder();
        // 回滚发布
        List<Branch> branches = branchDao.findLockByAppIdAndProfileId(order.getAppId(), order.getProfileId());
        Set<Long> targetReleaseVersions = branches.stream()
                .map(Branch::getReleaseVersion)
                .collect(Collectors.toSet());
        Releases.revertRelease(order.getAppId(), order.getProfileId(), order.getReleaseVersion(), targetReleaseVersions);
    }
}
