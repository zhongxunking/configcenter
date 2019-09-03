/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:31 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.BranchRules;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Profiles;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindAppSelfConfigOrder;
import org.antframework.configcenter.facade.result.FindAppSelfConfigResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找应用自己的在指定环境中的配置服务
 */
@Service
public class FindAppSelfConfigService {
    @ServiceExecute
    public void execute(ServiceContext<FindAppSelfConfigOrder, FindAppSelfConfigResult> context) {
        FindAppSelfConfigOrder order = context.getOrder();
        FindAppSelfConfigResult result = context.getResult();
        // 获取每个继承的环境中的配置
        for (ProfileInfo profile : Profiles.findInheritedProfiles(order.getProfileId())) {
            // 计算出分支
            String branchId = BranchRules.computeBranchRules(order.getAppId(), profile.getProfileId(), order.getTarget());
            BranchInfo branch = Branches.findBranch(order.getAppId(), profile.getProfileId(), branchId);
            if (branch == null) {
                throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), profile.getProfileId(), branchId));
            }
            // 获取发布
            ReleaseInfo release = branch.getRelease();
            // 移除作用域不合要求的配置
            release.getProperties().removeIf(property -> property.getScope().compareTo(order.getMinScope()) < 0);

            result.addInheritedRelease(release);
        }
    }
}
