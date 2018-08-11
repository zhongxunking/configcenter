/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:22 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.*;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.api.ProfileService;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.DeleteAppOrder;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.FindAppPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindAllProfilesResult;
import org.antframework.configcenter.facade.result.FindAppPropertyKeysResult;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 删除应用服务
 */
@Service(enableTx = true)
public class DeleteAppService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private PropertyKeyService propertyKeyService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<DeleteAppOrder, EmptyResult> context) {
        DeleteAppOrder order = context.getOrder();

        App app = appDao.findLockByAppId(order.getAppId());
        if (app == null) {
            return;
        }
        if (appDao.existsByParent(order.getAppId())) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("还存在以%s为父应用的应用，不能删除", order.getAppId()));
        }
        // 删除该应用的所有属性key
        for (PropertyKeyInfo propertyKey : getAppAllPropertyKeys(order.getAppId())) {
            deletePropertyKey(propertyKey);
        }
        // 删除应用
        appDao.delete(app);
    }

    // 获取应用的所有属性key
    private List<PropertyKeyInfo> getAppAllPropertyKeys(String appId) {
        FindAppPropertyKeysOrder order = new FindAppPropertyKeysOrder();
        order.setAppId(appId);
        order.setMinScope(Scope.PRIVATE);

        FindAppPropertyKeysResult result = propertyKeyService.findAppPropertyKeys(order);
        FacadeUtils.assertSuccess(result);
        return result.getPropertyKeys();
    }

    // 删除属性key
    private void deletePropertyKey(PropertyKeyInfo propertyKey) {
        DeletePropertyKeyOrder order = new DeletePropertyKeyOrder();
        order.setAppId(propertyKey.getAppId());
        order.setKey(propertyKey.getKey());

        EmptyResult result = propertyKeyService.deletePropertyKey(order);
        FacadeUtils.assertSuccess(result);
    }

    @ServiceAfter
    public void after(ServiceContext<DeleteAppOrder, EmptyResult> context) {
        DeleteAppOrder order = context.getOrder();
        // 删除所有环境中的应用节点
        for (ProfileInfo profile : getAllProfiles()) {
            zkTemplate.deleteNode(ZkTemplate.buildPath(profile.getProfileId(), order.getAppId()));
        }
    }

    // 获取所有环境
    private List<ProfileInfo> getAllProfiles() {
        FindAllProfilesResult result = profileService.findAllProfiles(new EmptyOrder());
        FacadeUtils.assertSuccess(result);
        return result.getProfiles();
    }
}
