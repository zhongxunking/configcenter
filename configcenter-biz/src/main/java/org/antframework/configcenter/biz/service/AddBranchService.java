/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:10 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.order.AddBranchOrder;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;

/**
 * 添加分支服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddBranchService {
    // 分支dao
    private final BranchDao branchDao;
    // 应用dao
    private final AppDao appDao;
    // 环境dao
    private final ProfileDao profileDao;
    // 发布dao
    private final ReleaseDao releaseDao;

    @ServiceBefore
    public void before(ServiceContext<AddBranchOrder, EmptyResult> context) {
        AddBranchOrder order = context.getOrder();
        // 校验分支
        Branch branch = branchDao.findByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch != null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]已存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<AddBranchOrder, EmptyResult> context) {
        AddBranchOrder order = context.getOrder();
        // 校验应用、环境、发布
        if (order.getReleaseVersion() == ReleaseConstant.ORIGIN_VERSION) {
            App app = appDao.findLockByAppId(order.getAppId());
            if (app == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
            }
            Profile profile = profileDao.findLockByProfileId(order.getProfileId());
            if (profile == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileId()));
            }
        } else {
            Release release = releaseDao.findLockByAppIdAndProfileIdAndVersion(order.getAppId(), order.getProfileId(), order.getReleaseVersion());
            if (release == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), order.getReleaseVersion()));
            }
        }
        // 新增分支
        Branch branch = new Branch();
        BeanUtils.copyProperties(order, branch);
        branchDao.save(branch);
    }
}
