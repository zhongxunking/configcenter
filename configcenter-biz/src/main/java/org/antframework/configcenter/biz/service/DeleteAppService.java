/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:22 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.*;
import org.antframework.configcenter.biz.util.PropertyKeyUtils;
import org.antframework.configcenter.biz.util.RefreshUtils;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.DeleteAppOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除应用服务
 */
@Service(enableTx = true)
public class DeleteAppService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private PropertyKeyService propertyKeyService;

    @ServiceExecute
    public void execute(ServiceContext<DeleteAppOrder, EmptyResult> context) {
        DeleteAppOrder order = context.getOrder();

        App app = appDao.findLockByAppId(order.getAppId());
        if (app == null) {
            return;
        }
        if (appDao.existsByParent(order.getAppId())) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("应用[%s]存在子应用，不能删除", order.getAppId()));
        }
        // 删除该应用的所有配置key
        for (PropertyKeyInfo propertyKey : PropertyKeyUtils.findAppPropertyKeys(order.getAppId(), Scope.PRIVATE)) {
            deletePropertyKey(propertyKey);
        }
        // 删除应用
        appDao.delete(app);
    }

    // 删除配置key
    private void deletePropertyKey(PropertyKeyInfo propertyKey) {
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppId(propertyKey.getAppId());
        order.setKey(propertyKey.getKey());

        EmptyResult result = propertyKeyService.deletePropertyKey(order);
        FacadeUtils.assertSuccess(result);
    }

    @ServiceAfter
    public void after(ServiceContext<DeleteAppOrder, EmptyResult> context) {
        // 刷新zookeeper
        RefreshUtils.refreshZk();
    }
}
