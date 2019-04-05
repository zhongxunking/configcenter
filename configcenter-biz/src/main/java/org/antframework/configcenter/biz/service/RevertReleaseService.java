/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-09 16:30 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.*;
import org.antframework.configcenter.biz.util.RefreshUtils;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.order.RevertReleaseOrder;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 回滚发布服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class RevertReleaseService {
    // 发布dao
    private final ReleaseDao releaseDao;
    // 配置value服务
    private final PropertyValueService propertyValueService;

    @ServiceExecute
    public void execute(ServiceContext<RevertReleaseOrder, EmptyResult> context) {
        RevertReleaseOrder order = context.getOrder();
        // 校验入参
        if (order.getTargetVersion() > ReleaseConstant.ORIGIN_VERSION) {
            Release release = releaseDao.findLockByAppIdAndProfileIdAndVersion(order.getAppId(), order.getProfileId(), order.getTargetVersion());
            if (release == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("回滚到的目标发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), order.getTargetVersion()));
            }
        }
        // 回滚发布
        releaseDao.deleteByAppIdAndProfileIdAndVersionGreaterThan(order.getAppId(), order.getProfileId(), order.getTargetVersion());
    }

    @ServiceAfter
    public void after(ServiceContext<RevertReleaseOrder, EmptyResult> context) {
        RevertReleaseOrder order = context.getOrder();
        // 回滚配置value
        revertPropertyValues(order.getAppId(), order.getProfileId(), order.getTargetVersion());
        // 刷新客户端
        RefreshUtils.refreshClients(order.getAppId(), order.getProfileId());
    }

    // 回滚配置value
    private void revertPropertyValues(String appId, String profileId, Long version) {
        RevertPropertyValuesOrder order = new RevertPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setReleaseVersion(version);

        EmptyResult result = propertyValueService.revertPropertyValues(order);
        FacadeUtils.assertSuccess(result);
    }
}
