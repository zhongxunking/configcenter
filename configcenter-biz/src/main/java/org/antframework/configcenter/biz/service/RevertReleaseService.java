/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-09 16:30 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Refreshes;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.Release;
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

    @ServiceExecute
    public void execute(ServiceContext<RevertReleaseOrder, EmptyResult> context) {
        RevertReleaseOrder order = context.getOrder();
        // 根据发布继承关系进行删除
        long version = order.getSourceVersion();
        while (version > ReleaseConstant.ORIGIN_VERSION
                && !order.getTargetVersions().contains(version)) {
            Release release = releaseDao.findLockByAppIdAndProfileIdAndVersion(order.getAppId(), order.getProfileId(), version);
            if (release == null) {
                throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), version));
            }
            boolean existsChildren = releaseDao.existsByAppIdAndProfileIdAndParentVersion(order.getAppId(), order.getProfileId(), version);
            if (existsChildren) {
                break;
            }
            releaseDao.delete(release);
            version = release.getParentVersion();
        }
    }

    @ServiceAfter
    public void after(ServiceContext<RevertReleaseOrder, EmptyResult> context) {
        RevertReleaseOrder order = context.getOrder();
        // 刷新客户端
        Refreshes.refreshClients(order.getAppId(), order.getProfileId());
    }
}
