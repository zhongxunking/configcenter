/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 16:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.common.ZkUtils;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.order.TriggerClientsRefreshOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 触发客户端刷新配置服务
 */
@Service
public class TriggerClientsRefreshService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<TriggerClientsRefreshOrder, EmptyResult> context) {
        TriggerClientsRefreshOrder order = context.getOrder();
        // 获取需要刷新的应用
        List<App> apps = getApps(order);
        // 刷新zookeeper
        for (Profile profile : getProfiles(order)) {
            for (App app : apps) {
                zkTemplate.setData(ZkTemplate.buildPath(profile.getProfileId(), app.getAppId()), ZkUtils.getCurrentDate());
            }
        }
    }

    // 获取需要刷新的应用
    private List<App> getApps(TriggerClientsRefreshOrder order) {
        List<App> apps = new ArrayList<>();
        if (order.getAppId() != null) {
            App app = appDao.findByAppId(order.getAppId());
            if (app == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]", order.getAppId()));
            }
            apps.add(app);
        }
        apps.addAll(getAllChildren(order.getAppId()));

        return apps;
    }

    // 获取所有子应用
    private List<App> getAllChildren(String appId) {
        List<App> children = appDao.findByParent(appId);
        List<App> allChildren = new ArrayList<>(children);
        for (App child : children) {
            allChildren.addAll(getAllChildren(child.getAppId()));
        }

        return allChildren;
    }

    // 获取需要刷新的环境
    private List<Profile> getProfiles(TriggerClientsRefreshOrder order) {
        List<Profile> profiles = new ArrayList<>();
        if (order.getProfileId() == null) {
            profiles.addAll(profileDao.findAll());
        } else {
            Profile profile = profileDao.findByProfileId(order.getProfileId());
            if (profile == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileId()));
            }
            profiles.add(profile);
        }

        return profiles;
    }
}
