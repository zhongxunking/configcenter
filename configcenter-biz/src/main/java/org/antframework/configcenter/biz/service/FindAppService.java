/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:44 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service
public class FindAppService {
    @Autowired
    private AppDao appDao;

    @ServiceExecute
    public void execute(ServiceContext<FindAppOrder, FindAppResult> serviceContext) {
        FindAppOrder order = serviceContext.getOrder();
        FindAppResult result = serviceContext.getResult();

        App app = appDao.findByAppCode(order.getAppCode());
        if (app != null) {
            result.setAppInfo(buildAppInfo(app));
        }
    }

    private AppInfo buildAppInfo(App app) {
        AppInfo appInfo = new AppInfo();
        BeanUtils.copyProperties(app, appInfo);
        return appInfo;
    }
}
