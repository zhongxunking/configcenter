/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.order.manage.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service(enableTx = true)
public class AddOrModifyAppService {
    @Autowired
    private AppDao appDao;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyAppOrder, AddOrModifyAppResult> serviceContext) {
        AddOrModifyAppOrder order = serviceContext.getOrder();

        App app = appDao.findLockByAppCode(order.getAppCode());
        if (app == null) {
            app = buildApp(order);
        } else {
            app.setMemo(order.getMemo());
        }
        appDao.save(app);
    }

    private App buildApp(AddOrModifyAppOrder addOrModifyAppOrder) {
        App app = new App();
        BeanUtils.copyProperties(addOrModifyAppOrder, app);
        return app;
    }
}
