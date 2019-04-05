/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-22 23:11 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.FindInheritedAppsOrder;
import org.antframework.configcenter.facade.result.FindInheritedAppsResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找应用继承的所有应用服务
 */
@Service
public class FindInheritedAppsService {
    @ServiceExecute
    public void execute(ServiceContext<FindInheritedAppsOrder, FindInheritedAppsResult> context) {
        FindInheritedAppsOrder order = context.getOrder();
        FindInheritedAppsResult result = context.getResult();

        String appId = order.getAppId();
        while (appId != null) {
            AppInfo app = AppUtils.findApp(appId);
            if (app == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", appId));
            }
            result.addInheritedApp(app);

            appId = app.getParent();
        }
    }
}
