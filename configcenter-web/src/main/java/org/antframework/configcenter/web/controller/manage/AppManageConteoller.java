/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 10:48 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.AbstractQueryResult;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.api.manage.AppManageService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.manage.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.order.manage.DeleteAppOrder;
import org.antframework.configcenter.facade.order.manage.QueryAppOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyAppResult;
import org.antframework.configcenter.facade.result.manage.DeleteAppResult;
import org.antframework.configcenter.web.manager.facade.api.ManagerAppManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用管理controller
 */
@RestController
@RequestMapping("/manage/app")
public class AppManageConteoller extends AbstractController {
    @Autowired
    private AppManageService appManageService;
    @Autowired
    private ManagerAppManageService managerAppManageService;
    @Autowired
    private ConfigService configService;

    /**
     * 添加或修改应用
     *
     * @param appCode 应用编码（必须）
     * @param memo    备注（可选）
     */
    @RequestMapping("/addOrModifyApp")
    public AddOrModifyAppResult addOrModifyApp(String appCode, String memo) {
        assertAdmin();
        AddOrModifyAppOrder order = new AddOrModifyAppOrder();
        order.setAppCode(appCode);
        order.setMemo(memo);

        return appManageService.addOrModifyApp(order);
    }

    /**
     * 删除应用
     *
     * @param appCode 应用编码（必须）
     */
    @RequestMapping("/deleteApp")
    public DeleteAppResult deleteApp(String appCode) {
        assertAdmin();
        DeleteAppOrder order = new DeleteAppOrder();
        order.setAppCode(appCode);

        return appManageService.deleteApp(order);
    }

    /**
     * 分页查询应用
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param appCode  应用编码（可选，有值会进行模糊查询）
     */
    @RequestMapping("/queryApp")
    public AbstractQueryResult<AppInfo> queryApp(int pageNo, int pageSize, String appCode) {
        assertAdmin();
        QueryAppOrder order = new QueryAppOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppCode(appCode);
        return appManageService.queryApp(order);
    }

//    // 超级管理员查询应用
//    private QueryAppResult queryAppByAdmin(int pageNo, int pageSize, String appCode) {
//        QueryAppOrder order = new QueryAppOrder();
//        order.setPageNo(pageNo);
//        order.setPageSize(pageSize);
//        order.setAppCode(appCode);
//        return appManageService.queryApp(order);
//    }
//
//    // 普通管理员查询应用
//    private AbstractQueryResult<AppInfo> queryAppByNormalManager(int pageNo, int pageSize, String appCode) {
//        QueryManagedAppOrder order = new QueryManagedAppOrder();
//        order.setPageNo(pageNo);
//        order.setPageSize(pageSize);
//        order.setManagerCode(SessionAccessor.getManagerInfo().getManagerCode());
//        order.setAppCode(appCode);
//        QueryManagedAppResult queryManagedAppResult = managerAppManageService.queryManagedApp(order);
//        if (!queryManagedAppResult.isSuccess()) {
//            throw new AntBekitException(Status.FAIL, queryManagedAppResult.getCode(), queryManagedAppResult.getMessage());
//        }
//
//        AbstractQueryResult<AppInfo> result = new AbstractQueryResult<AppInfo>() {
//        };
//        result.setStatus(Status.SUCCESS);
//        result.setCode(CommonResultCode.SUCCESS.getCode());
//        result.setMessage(CommonResultCode.SUCCESS.getMessage());
//        result.setTotalCount(queryManagedAppResult.getTotalCount());
//        for (AppInfo appInfo : getAppInfos(queryManagedAppResult.getInfos())) {
//            result.addInfo(appInfo);
//        }
//
//        return result;
//    }
//
//    // 获取应用信息
//    private List<AppInfo> getAppInfos(List<ManagerAppInfo> managerAppInfos) {
//        List<AppInfo> appInfos = new ArrayList<>();
//        for (ManagerAppInfo info : managerAppInfos) {
//            FindAppOrder order = new FindAppOrder();
//            order.setAppCode(info.getAppCode());
//            FindAppResult result = configService.findApp(order);
//            if (!result.isSuccess()) {
//                throw new AntBekitException(Status.FAIL, result.getCode(), result.getMessage());
//            }
//            appInfos.add(result.getAppInfo());
//        }
//
//        return appInfos;
//    }
}
