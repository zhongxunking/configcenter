/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 16:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.common.ZkUtils;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.order.manage.TriggerClientRefreshOrder;
import org.antframework.configcenter.facade.result.manage.TriggerClientRefreshResult;
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
public class TriggerClientRefreshService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<TriggerClientRefreshOrder, TriggerClientRefreshResult> context) {
        TriggerClientRefreshOrder order = context.getOrder();

        List<App> apps = getApps(order);
        List<Profile> profiles = getProfiles(order);
        for (Profile profile : profiles) {
            for (App app : apps) {
                zkTemplate.setData(ZkTemplate.buildPath(profile.getProfileCode(), app.getAppCode()), ZkUtils.getCurrentDate());
            }
        }
    }

    // 获取需要刷新的应用
    private List<App> getApps(TriggerClientRefreshOrder order) {
        List<App> apps = new ArrayList<>();
        if (order.getAppCode() == null) {
            apps.addAll(appDao.findAll());
        } else {
            App app = appDao.findByAppCode(order.getAppCode());
            if (app == null) {
                throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]", order.getAppCode()));
            }
            apps.add(app);
        }

        return apps;
    }

    // 获取需要刷新的环境
    private List<Profile> getProfiles(TriggerClientRefreshOrder order) {
        List<Profile> profiles = new ArrayList<>();
        if (order.getProfileCode() == null) {
            profiles.addAll(profileDao.findAll());
        } else {
            Profile profile = profileDao.findByProfileCode(order.getProfileCode());
            if (profile == null) {
                throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileCode()));
            }
            profiles.add(profile);
        }

        return profiles;
    }
}
