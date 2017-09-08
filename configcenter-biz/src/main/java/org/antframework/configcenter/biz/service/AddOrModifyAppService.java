/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.biz.ZkOperations;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.order.manage.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 添加或修改应用服务
 */
@Service(enableTx = true)
public class AddOrModifyAppService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private ZkOperations zkOperations;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyAppOrder, AddOrModifyAppResult> context) {
        AddOrModifyAppOrder order = context.getOrder();

        App app = appDao.findLockByAppCode(order.getAppCode());
        if (app == null) {
            app = buildApp(order);
        } else {
            app.setMemo(order.getMemo());
        }
        appDao.save(app);
    }

    @ServiceAfter
    public void after(ServiceContext<AddOrModifyAppOrder, AddOrModifyAppResult> context) {
        AddOrModifyAppOrder order = context.getOrder();

        List<Profile> profiles = profileDao.findAll();
        for (Profile profile : profiles) {
            zkOperations.createNodesByPath(ZkOperations.buildPath(profile.getProfileCode(), order.getAppCode()));
        }
    }

    // 构建应用
    private App buildApp(AddOrModifyAppOrder addOrModifyAppOrder) {
        App app = new App();
        BeanUtils.copyProperties(addOrModifyAppOrder, app);
        return app;
    }
}
