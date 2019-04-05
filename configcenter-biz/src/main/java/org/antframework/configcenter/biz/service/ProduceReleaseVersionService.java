/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 17:23 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.order.ProduceReleaseVersionOrder;
import org.antframework.configcenter.facade.result.ProduceReleaseVersionResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 生产发布版本-服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class ProduceReleaseVersionService {
    // 应用dao
    private final AppDao appDao;

    @ServiceExecute
    public void execute(ServiceContext<ProduceReleaseVersionOrder, ProduceReleaseVersionResult> context) {
        ProduceReleaseVersionOrder order = context.getOrder();
        ProduceReleaseVersionResult result = context.getResult();

        App app = appDao.findLockByAppId(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
        long version = app.getReleaseVersion() + 1;
        app.setReleaseVersion(version);
        appDao.save(app);

        result.setReleaseVersion(version);
    }
}
