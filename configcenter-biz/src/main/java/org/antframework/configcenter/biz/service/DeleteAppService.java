/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:22 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Profiles;
import org.antframework.configcenter.biz.util.PropertyKeys;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.DeleteAppOrder;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 删除应用服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteAppService {
    // 应用dao
    private final AppDao appDao;

    @ServiceExecute
    public void execute(ServiceContext<DeleteAppOrder, EmptyResult> context) {
        DeleteAppOrder order = context.getOrder();
        // 校验
        App app = appDao.findLockByAppId(order.getAppId());
        if (app == null) {
            return;
        }
        if (appDao.existsByParent(order.getAppId())) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("应用[%s]存在子应用，不能删除", order.getAppId()));
        }
        // 删除该应用的所有配置key
        for (PropertyKeyInfo propertyKey : PropertyKeys.findPropertyKeys(order.getAppId(), Scope.PRIVATE)) {
            PropertyKeys.deletePropertyKey(propertyKey.getAppId(), propertyKey.getKey());
        }
        // 删除该应用在所有环境的所有分支
        for (ProfileInfo profile : Profiles.findAllProfiles()) {
            for (BranchInfo branch : Branches.findBranches(order.getAppId(), profile.getProfileId())) {
                Branches.deleteBranch(order.getAppId(), profile.getProfileId(), branch.getBranchId());
            }
        }
        // 删除应用
        appDao.delete(app);
    }
}
