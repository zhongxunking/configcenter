/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 10:48 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.AbstractQueryResult;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.AppService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.order.DeleteAppOrder;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.order.QueryAppsOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.QueryAppsResult;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.facade.info.ManagerInfo;
import org.antframework.manager.facade.info.RelationInfo;
import org.antframework.manager.facade.result.QueryManagerRelationResult;
import org.antframework.manager.web.common.ManagerAssert;
import org.antframework.manager.web.common.Managers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用管理controller
 */
@RestController
@RequestMapping("/manage/app")
public class AppManageController {
    @Autowired
    private AppService appService;

    /**
     * 添加或修改应用
     *
     * @param appId 应用id（必须）
     * @param memo  备注（可选）
     */
    @RequestMapping("/addOrModifyApp")
    public EmptyResult addOrModifyApp(String appId, String memo) {
        ManagerAssert.admin();
        AddOrModifyAppOrder order = new AddOrModifyAppOrder();
        order.setAppId(appId);
        order.setMemo(memo);

        return appService.addOrModifyApp(order);
    }

    /**
     * 删除应用
     *
     * @param appId 应用id（必须）
     */
    @RequestMapping("/deleteApp")
    public EmptyResult deleteApp(String appId) {
        ManagerAssert.admin();
        // 删除管理员和应用的关联
        Managers.deleteAllRelationsByTarget(appId);
        // 删除应用
        DeleteAppOrder order = new DeleteAppOrder();
        order.setAppId(appId);

        return appService.deleteApp(order);
    }

    /**
     * 查找应用
     *
     * @param appId 应用id
     */
    @RequestMapping("/findApp")
    public FindAppResult findApp(String appId) {
        FindAppOrder order = new FindAppOrder();
        order.setAppId(appId);

        return appService.findApp(order);
    }

    /**
     * 分页查询应用
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param appId    应用id（可选）
     */
    @RequestMapping("/queryApps")
    public QueryAppsResult queryApps(int pageNo, int pageSize, String appId) {
        ManagerAssert.admin();
        QueryAppsOrder order = new QueryAppsOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppId(appId);

        return appService.queryApps(order);
    }

    /**
     * 查询被管理的应用
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param appId    应用id（可选）
     */
    @RequestMapping("/queryManagedApp")
    public QueryManagedAppResult queryManagedApp(int pageNo, int pageSize, String appId) {
        ManagerInfo manager = ManagerAssert.currentManager();
        if (manager.getType() == ManagerType.ADMIN) {
            return forAdmin(pageNo, pageSize, appId);
        } else {
            return forNormal(Managers.queryManagerRelation(pageNo, pageSize, appId));
        }
    }

    // 为超级管理员查询所有的应用
    private QueryManagedAppResult forAdmin(int pageNo, int pageSize, String appId) {
        QueryAppsResult queryAppsResult = queryApps(pageNo, pageSize, appId);
        // 构建返回结果
        QueryManagedAppResult result = new QueryManagedAppResult();
        BeanUtils.copyProperties(queryAppsResult, result, "infos");
        result.getInfos().addAll(queryAppsResult.getInfos());
        return result;
    }

    // 查询被普通管理管理的应用
    private QueryManagedAppResult forNormal(QueryManagerRelationResult relationResult) {
        QueryManagedAppResult result = new QueryManagedAppResult();
        BeanUtils.copyProperties(relationResult, result, "infos");
        // 根据关系查找应用
        for (RelationInfo relationInfo : relationResult.getInfos()) {
            AppInfo appInfo = findAppInfo(relationInfo.getTargetId());
            if (appInfo != null) {
                result.addInfo(appInfo);
            }
        }
        return result;
    }

    // 查找应用
    private AppInfo findAppInfo(String appId) {
        FindAppResult result = findApp(appId);
        if (!result.isSuccess()) {
            throw new BizException(Status.FAIL, result.getCode(), result.getMessage());
        }
        return result.getAppInfo();
    }

    /**
     * 查询被管理的应用result
     */
    public static class QueryManagedAppResult extends AbstractQueryResult<AppInfo> {
    }
}
