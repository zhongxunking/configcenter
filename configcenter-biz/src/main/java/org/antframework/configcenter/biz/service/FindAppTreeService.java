/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-22 21:55 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.AppTree;
import org.antframework.configcenter.facade.order.FindAppTreeOrder;
import org.antframework.configcenter.facade.result.FindAppTreeResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找应用树服务
 */
@Service
public class FindAppTreeService {
    // info转换器
    private static final Converter<App, AppInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(AppInfo.class);

    @Autowired
    private AppDao appDao;

    @ServiceExecute
    private void execute(ServiceContext<FindAppTreeOrder, FindAppTreeResult> context) {
        FindAppTreeOrder order = context.getOrder();
        FindAppTreeResult result = context.getResult();

        App app = null;
        if (order.getAppId() != null) {
            app = appDao.findByAppId(order.getAppId());
            if (app == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]", order.getAppId()));
            }
        }
        result.setAppTree(getAppTree(app));
    }

    // 获取应用树
    private AppTree getAppTree(App app) {
        AppInfo appInfo = app == null ? null : INFO_CONVERTER.convert(app);
        AppTree appTree = new AppTree(appInfo);

        List<App> childrenApp = appDao.findByParent(app == null ? null : app.getAppId());
        for (App childApp : childrenApp) {
            appTree.addChild(getAppTree(childApp));
        }

        return appTree;
    }
}
