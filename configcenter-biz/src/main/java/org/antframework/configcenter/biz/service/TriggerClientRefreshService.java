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
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.manage.TriggerClientRefreshOrder;
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
    public void execute(ServiceContext<TriggerClientRefreshOrder, EmptyResult> context) {
        TriggerClientRefreshOrder order = context.getOrder();

        App app = getApp(order);
        List<Profile> profiles = getProfiles(order);
        for (Profile profile : profiles) {
            zkTemplate.setData(ZkTemplate.buildPath(profile.getProfileCode(), app.getAppCode()), ZkUtils.getCurrentDate());
        }
    }

    // 获取需要刷新的应用
    private App getApp(TriggerClientRefreshOrder order) {
        String appCode = order.getAppCode();
        if (appCode == null) {
            appCode = ConfigService.COMMON_APP_CODE;
        }
        App app = appDao.findByAppCode(appCode);
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在应用[%s]", appCode));
        }
        return app;
    }

    // 获取需要刷新的环境
    private List<Profile> getProfiles(TriggerClientRefreshOrder order) {
        List<Profile> profiles = new ArrayList<>();
        if (order.getProfileCode() == null) {
            profiles.addAll(profileDao.findAll());
        } else {
            Profile profile = profileDao.findByProfileCode(order.getProfileCode());
            if (profile == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileCode()));
            }
            profiles.add(profile);
        }

        return profiles;
    }
}
