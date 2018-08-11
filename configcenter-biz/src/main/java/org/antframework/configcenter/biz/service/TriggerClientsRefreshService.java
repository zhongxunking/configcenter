/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 16:05 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.*;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.facade.api.AppService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.AppTree;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.FindAppTreeOrder;
import org.antframework.configcenter.facade.order.TriggerClientsRefreshOrder;
import org.antframework.configcenter.facade.result.FindAppTreeResult;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 触发客户端刷新配置服务
 */
@Service
public class TriggerClientsRefreshService {
    @Autowired
    private AppService appService;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<TriggerClientsRefreshOrder, EmptyResult> context) {
        TriggerClientsRefreshOrder order = context.getOrder();
        // 获取需要刷新的应用
        List<AppInfo> apps = new ArrayList<>();
        extractApps(getAppTree(order.getAppId()), apps);
        // 刷新zookeeper
        byte[] data = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS").getBytes(Charset.forName("utf-8"));
        for (String profileId : getProfileIds(order)) {
            for (AppInfo app : apps) {
                zkTemplate.setData(ZkTemplate.buildPath(profileId, app.getAppId()), data);
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

    // 获取应用树
    private AppTree getAppTree(String appId) {
        FindAppTreeOrder order = new FindAppTreeOrder();
        order.setAppId(appId);

        FindAppTreeResult result = appService.findAppTree(order);
        FacadeUtils.assertSuccess(result);
        return result.getAppTree();
    }

    // 获取需要刷新的环境id
    private List<String> getProfileIds(TriggerClientsRefreshOrder order) {
        List<String> allProfileIds = new ArrayList<>();
        for (ProfileInfo profile : ProfileUtils.findAllProfiles()) {
            allProfileIds.add(profile.getProfileId());
        }
        if (order.getProfileId() == null) {
            return allProfileIds;
        }
        if (!allProfileIds.contains(order.getProfileId())) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", order.getProfileId()));
        }
        return Arrays.asList(order.getProfileId());
    }
}
