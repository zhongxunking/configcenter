/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:40 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.api.AppService;
import org.antframework.configcenter.facade.api.PropertyKeyService;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.DeleteProfileOrder;
import org.antframework.configcenter.facade.order.FindAppPropertyKeysOrder;
import org.antframework.configcenter.facade.order.QueryAppsOrder;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppPropertyKeysResult;
import org.antframework.configcenter.facade.result.QueryAppsResult;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除环境服务
 */
@Service(enableTx = true)
public class DeleteProfileService {
    // 分页查询应用使用的每页大小
    private static final int QUERY_APPS_PAGE_SIZE = 100;

    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private AppService appService;
    @Autowired
    private PropertyKeyService propertyKeyService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<DeleteProfileOrder, EmptyResult> context) {
        DeleteProfileOrder order = context.getOrder();

        Profile profile = profileDao.findLockByProfileId(order.getProfileId());
        if (profile == null) {
            return;
        }
        // 删除所有应用在该环境下的属性值
        for (AppInfo app : getAllApps()) {
            deleteAppProfileAllPropertyValues(app.getAppId(), order.getProfileId());
        }
        // 删除环境
        profileDao.delete(profile);
    }

    @ServiceAfter
    public void after(ServiceContext<DeleteProfileOrder, EmptyResult> context) {
        DeleteProfileOrder order = context.getOrder();
        // 删除环境节点
        zkTemplate.deleteNode(ZkTemplate.buildPath(order.getProfileId()));
    }

    // 获取所有应用
    private List<AppInfo> getAllApps() {
        List<AppInfo> apps = new ArrayList<>();

        int pageNo = 1;
        QueryAppsResult result;
        do {
            result = appService.queryApps(buildQueryAppsOrder(pageNo++));
            FacadeUtils.assertSuccess(result);
            apps.addAll(result.getInfos());
        } while (pageNo <= FacadeUtils.calcTotalPage(result.getTotalCount(), QUERY_APPS_PAGE_SIZE));

        return apps;
    }

    // 构建查询应用的order
    private QueryAppsOrder buildQueryAppsOrder(int pageNo) {
        QueryAppsOrder order = new QueryAppsOrder();
        order.setPageNo(pageNo);
        order.setPageSize(QUERY_APPS_PAGE_SIZE);
        order.setAppId(null);
        order.setParent(null);

        return order;
    }

    // 删除应有在指定环境的所有属性value
    private void deleteAppProfileAllPropertyValues(String appId, String profileId) {
        SetPropertyValuesOrder order = new SetPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        for (PropertyKeyInfo propertyKey : getAppAllPropertyKeys(appId)) {
            SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
            keyValue.setKey(propertyKey.getKey());
            keyValue.setValue(null);

            order.addKeyValue(keyValue);
        }

        EmptyResult result = propertyValueService.setPropertyValues(order);
        FacadeUtils.assertSuccess(result);
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
}
