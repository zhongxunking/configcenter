/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 10:48 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.AbstractQueryResult;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.manage.AppManageService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.manage.AddOrModifyAppOrder;
import org.antframework.configcenter.facade.order.manage.DeleteAppOrder;
import org.antframework.configcenter.facade.order.manage.QueryAppOrder;
import org.antframework.configcenter.facade.result.manage.DeleteAppResult;
import org.antframework.configcenter.web.manager.facade.api.ManagerAppManageService;
import org.antframework.configcenter.web.manager.facade.order.DeleteManagerAppByAppOrder;
import org.antframework.configcenter.web.manager.facade.result.DeleteManagerAppByAppResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用管理controller
 */
@RestController
@RequestMapping("/manage/app")
public class AppManageController extends AbstractController {
    @Autowired
    private AppManageService appManageService;
    @Autowired
    private ManagerAppManageService managerAppManageService;

    /**
     * 添加或修改应用
     *
     * @param appCode 应用编码（必须）
     * @param memo    备注（可选）
     */
    @RequestMapping("/addOrModifyApp")
    public EmptyResult addOrModifyApp(String appCode, String memo) {
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
        // 先删除管理员和应用关联
        DeleteManagerAppByAppOrder byAppOrder = new DeleteManagerAppByAppOrder();
        byAppOrder.setAppCode(appCode);
        DeleteManagerAppByAppResult byAppResult = managerAppManageService.deleteManagerAppByApp(byAppOrder);
        if (!byAppResult.isSuccess()) {
            throw new AntBekitException(Status.FAIL, byAppResult.getCode(), byAppResult.getMessage());
        }
        // 删除应用
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
}
