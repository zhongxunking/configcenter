/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 16:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.AppTree;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ProfileTree;
import org.antframework.configcenter.facade.order.RefreshClientsOrder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 刷新客户端服务
 */
@Service
public class RefreshClientsService {
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<RefreshClientsOrder, EmptyResult> context) {
        RefreshClientsOrder order = context.getOrder();
        // 获取需要刷新的应用
        List<AppInfo> apps = new ArrayList<>();
        extractApps(AppUtils.findAppTree(order.getRootAppId()), apps);
        // 获取需要刷新的环境
        List<ProfileInfo> profiles = new ArrayList<>();
        extractProfiles(ProfileUtils.findProfileTree(order.getRootProfileId()), profiles);
        // 刷新zookeeper
        byte[] data = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS").getBytes(Charset.forName("utf-8"));
        for (ProfileInfo profile : profiles) {
            for (AppInfo app : apps) {
                zkTemplate.setData(ZkTemplate.buildPath(profile.getProfileId(), app.getAppId()), data);
            }
        }
    }

    // 提取出应用
    private void extractApps(AppTree appTree, List<AppInfo> target) {
        if (appTree.getApp() != null) {
            target.add(appTree.getApp());
        }
        for (AppTree child : appTree.getChildren()) {
            extractApps(child, target);
        }
    }

    // 提取出环境
    private void extractProfiles(ProfileTree profileTree, List<ProfileInfo> target) {
        if (profileTree.getProfile() != null) {
            target.add(profileTree.getProfile());
        }
        for (ProfileTree child : profileTree.getChildren()) {
            extractProfiles(child, target);
        }
    }
}
