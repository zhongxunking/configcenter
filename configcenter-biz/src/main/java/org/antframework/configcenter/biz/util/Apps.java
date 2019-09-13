/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-11 13:13 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.AppService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.AppTree;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用操作类
 */
public final class Apps {
    // 分页查询应用使用的每页大小
    private static final int QUERY_PAGE_SIZE = 100;
    // 应用服务
    private static final AppService APP_SERVICE = Contexts.getApplicationContext().getBean(AppService.class);

    /**
     * 生产发布版本
     *
     * @param appId 应用id
     * @return 版本
     */
    public static long produceReleaseVersion(String appId) {
        ProduceReleaseVersionOrder order = new ProduceReleaseVersionOrder();
        order.setAppId(appId);

        ProduceReleaseVersionResult result = APP_SERVICE.produceReleaseVersion(order);
        FacadeUtils.assertSuccess(result);
        return result.getReleaseVersion();
    }

    /**
     * 查找应用
     *
     * @param appId 应用id
     * @return 查找到的应用（null表示无该应用）
     */
    public static AppInfo findApp(String appId) {
        FindAppOrder order = new FindAppOrder();
        order.setAppId(appId);

        FindAppResult result = APP_SERVICE.findApp(order);
        FacadeUtils.assertSuccess(result);
        return result.getApp();
    }

    /**
     * 查找应用继承的所有应用
     *
     * @param appId 被查询的应用id
     * @return 由近及远继承的所有应用
     */
    public static List<AppInfo> findInheritedApps(String appId) {
        FindInheritedAppsOrder order = new FindInheritedAppsOrder();
        order.setAppId(appId);

        FindInheritedAppsResult result = APP_SERVICE.findInheritedApps(order);
        FacadeUtils.assertSuccess(result);
        return result.getInheritedApps();
    }

    /**
     * 获取环境树
     *
     * @param rootAppId 根节点应用id（null表示查找所有应用）
     * @return 应用树
     */
    public static AppTree findAppTree(String rootAppId) {
        FindAppTreeOrder order = new FindAppTreeOrder();
        order.setRootAppId(rootAppId);

        FindAppTreeResult result = APP_SERVICE.findAppTree(order);
        FacadeUtils.assertSuccess(result);
        return result.getAppTree();
    }

    /**
     * 查找所有应用
     *
     * @return 所有应用
     */
    public static List<AppInfo> findAllApps() {
        List<AppInfo> apps = new ArrayList<>();

        int pageNo = 1;
        QueryAppsResult result;
        do {
            result = APP_SERVICE.queryApps(buildQueryAppsOrder(pageNo++));
            FacadeUtils.assertSuccess(result);
            apps.addAll(result.getInfos());
        } while (pageNo <= FacadeUtils.calcTotalPage(result.getTotalCount(), QUERY_PAGE_SIZE));

        return apps;
    }

    // 构建查询应用的order
    private static QueryAppsOrder buildQueryAppsOrder(int pageNo) {
        QueryAppsOrder order = new QueryAppsOrder();
        order.setPageNo(pageNo);
        order.setPageSize(QUERY_PAGE_SIZE);
        order.setAppId(null);
        order.setParent(null);

        return order;
    }
}
