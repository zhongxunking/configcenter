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
import org.antframework.configcenter.facade.order.QueryAppOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.QueryAppResult;
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
     * @param appCode 应用编码（必须）
     * @param memo    备注（可选）
     */
    @RequestMapping("/addOrModifyApp")
    public EmptyResult addOrModifyApp(String appCode, String memo) {
        ManagerAssert.admin();
        AddOrModifyAppOrder order = new AddOrModifyAppOrder();
        order.setAppCode(appCode);
        order.setMemo(memo);

        return appService.addOrModifyApp(order);
    }

    /**
     * 删除应用
     *
     * @param appCode 应用编码（必须）
     */
    @RequestMapping("/deleteApp")
    public EmptyResult deleteApp(String appCode) {
        ManagerAssert.admin();
        // 删除管理员和应用的关联
        Managers.deleteAllRelationsByTarget(appCode);
        // 删除应用
        DeleteAppOrder order = new DeleteAppOrder();
        order.setAppCode(appCode);

        return appService.deleteApp(order);
    }

    /**
     * 查找应用
     *
     * @param appCode 应用编码
     */
    @RequestMapping("/findApp")
    public FindAppResult findApp(String appCode) {
        FindAppOrder order = new FindAppOrder();
        order.setAppCode(appCode);

        return appService.findApp(order);
    }

    /**
     * 查询被管理的应用
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param appCode  应用编码（可选）
     */
    @RequestMapping("/queryManagedApp")
    public QueryManagedAppResult queryManagedApp(int pageNo, int pageSize, String appCode) {
        ManagerInfo manager = ManagerAssert.currentManager();
        if (manager.getType() == ManagerType.ADMIN) {
            return forAdmin(pageNo, pageSize, appCode);
        } else {
            return forNormal(Managers.queryManagerRelation(pageNo, pageSize, appCode));
        }
    }

    // 为超级管理员查询所有的应用
    private QueryManagedAppResult forAdmin(int pageNo, int pageSize, String appCode) {
        QueryAppOrder order = new QueryAppOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppCode(appCode);
        QueryAppResult queryAppResult = appService.queryApp(order);
        // 构建返回结果
        QueryManagedAppResult result = new QueryManagedAppResult();
        BeanUtils.copyProperties(queryAppResult, result, "infos");
        result.getInfos().addAll(queryAppResult.getInfos());
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
    private AppInfo findAppInfo(String appCode) {
        FindAppResult result = findApp(appCode);
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
