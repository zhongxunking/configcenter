/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 10:48 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.AbstractQueryResult;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.AppService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.FindAppTreeResult;
import org.antframework.configcenter.facade.result.FindInheritedAppsResult;
import org.antframework.configcenter.facade.result.QueryAppsResult;
import org.antframework.configcenter.web.common.KeyPrivileges;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.facade.info.ManagerInfo;
import org.antframework.manager.web.Managers;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用管理controller
 */
@RestController
@RequestMapping("/manage/app")
@AllArgsConstructor
public class AppController {
    // 应用服务
    private final AppService appService;

    /**
     * 添加或修改应用
     *
     * @param appId   应用id（必须）
     * @param appName 应用名（可选）
     * @param parent  父应用id（可选）
     */
    @RequestMapping("/addOrModifyApp")
    public EmptyResult addOrModifyApp(String appId, String appName, String parent) {
        Managers.admin();
        AddOrModifyAppOrder order = new AddOrModifyAppOrder();
        order.setAppId(appId);
        order.setAppName(appName);
        order.setParent(parent);

        return appService.addOrModifyApp(order);
    }

    /**
     * 删除应用
     *
     * @param appId 应用id（必须）
     */
    @RequestMapping("/deleteApp")
    public EmptyResult deleteApp(String appId) {
        Managers.admin();
        // 删除管理员和应用的关联
        ManagerApps.deletesByApp(appId);
        // 删除应用
        DeleteAppOrder order = new DeleteAppOrder();
        order.setAppId(appId);
        EmptyResult result = appService.deleteApp(order);
        if (result.isSuccess()) {
            // 删除应用的所有配置key的权限
            KeyPrivileges.deletePrivileges(appId, null);
        }
        return result;
    }

    /**
     * 查找应用
     *
     * @param appId 应用id（必须）
     */
    @RequestMapping("/findApp")
    public FindAppResult findApp(String appId) {
        Managers.currentManager();
        FindAppOrder order = new FindAppOrder();
        order.setAppId(appId);

        return appService.findApp(order);
    }

    /**
     * 查找应用继承的所有应用
     *
     * @param appId 应用id（必须）
     */
    @RequestMapping("/findInheritedApps")
    public FindInheritedAppsResult findInheritedApps(String appId) {
        ManagerApps.adminOrHaveApp(appId);
        FindInheritedAppsOrder order = new FindInheritedAppsOrder();
        order.setAppId(appId);

        return appService.findInheritedApps(order);
    }

    /**
     * 查找应用树
     *
     * @param appId 根节点应用id（不填表示查找所有应用）
     */
    @RequestMapping("/findAppTree")
    private FindAppTreeResult findAppTree(String appId) {
        ManagerApps.adminOrHaveApp(appId);
        FindAppTreeOrder order = new FindAppTreeOrder();
        order.setAppId(appId);

        return appService.findAppTree(order);
    }

    /**
     * 分页查询应用
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param appId    应用id（可选）
     * @param parent   父应用id（可选）
     */
    @RequestMapping("/queryApps")
    public QueryAppsResult queryApps(int pageNo, int pageSize, String appId, String parent) {
        Managers.admin();
        QueryAppsOrder order = new QueryAppsOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppId(appId);
        order.setParent(parent);

        return appService.queryApps(order);
    }

    /**
     * 查询被管理的应用
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param appId    应用id（可选）
     */
    @RequestMapping("/queryManagedApps")
    public QueryManagedAppsResult queryManagedApps(int pageNo, int pageSize, String appId) {
        ManagerInfo manager = Managers.currentManager();
        if (manager.getType() == ManagerType.ADMIN) {
            return forAdmin(pageNo, pageSize, appId);
        } else {
            return forNormal(ManagerApps.queryManagedApps(pageNo, pageSize, appId));
        }
    }

    // 为超级管理员查询所有的应用
    private QueryManagedAppsResult forAdmin(int pageNo, int pageSize, String appId) {
        QueryAppsResult queryAppsResult = queryApps(pageNo, pageSize, appId, null);
        // 构建返回结果
        QueryManagedAppsResult result = new QueryManagedAppsResult();
        BeanUtils.copyProperties(queryAppsResult, result, "infos");
        result.getInfos().addAll(queryAppsResult.getInfos());
        return result;
    }

    // 为普通管理员查询他管理的应用
    private QueryManagedAppsResult forNormal(AbstractQueryResult<String> appIdsResult) {
        QueryManagedAppsResult result = new QueryManagedAppsResult();
        BeanUtils.copyProperties(appIdsResult, result, "infos");
        // 查找应用
        for (String appId : appIdsResult.getInfos()) {
            FindAppResult findAppResult = findApp(appId);
            FacadeUtils.assertSuccess(findAppResult);
            if (findAppResult.getApp() != null) {
                result.addInfo(findAppResult.getApp());
            }
        }
        return result;
    }

    /**
     * 查询被管理的应用result
     */
    public static class QueryManagedAppsResult extends AbstractQueryResult<AppInfo> {
    }
}
