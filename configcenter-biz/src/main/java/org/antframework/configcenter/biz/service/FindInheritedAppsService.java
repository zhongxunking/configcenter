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
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.FindInheritedAppsOrder;
import org.antframework.configcenter.facade.result.FindInheritedAppsResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * 查找应用继承的所有应用服务
 */
@Service
public class FindInheritedAppsService {
    // info转换器
    private static final Converter<App, AppInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(AppInfo.class);

    @Autowired
    private AppDao appDao;

    @ServiceExecute
    public void execute(ServiceContext<FindInheritedAppsOrder, FindInheritedAppsResult> context) {
        FindInheritedAppsOrder order = context.getOrder();
        FindInheritedAppsResult result = context.getResult();

        String appId = order.getAppId();
        while (appId != null) {
            App app = appDao.findByAppId(appId);
            if (app == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]", appId));
            }
            result.addInheritedAppInfo(INFO_CONVERTER.convert(app));
        }
    }
}
