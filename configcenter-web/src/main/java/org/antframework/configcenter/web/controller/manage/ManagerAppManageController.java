/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 21:51 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.web.manager.facade.api.ManagerAppManageService;
import org.antframework.configcenter.web.manager.facade.order.AddManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.order.DeleteManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.order.QueryManagedAppOrder;
import org.antframework.configcenter.web.manager.facade.order.QueryManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.result.DeleteManagerAppResult;
import org.antframework.configcenter.web.manager.facade.result.QueryManagedAppResult;
import org.antframework.configcenter.web.manager.facade.result.QueryManagerAppResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理管理员与应用关联的controller
 */
@RestController
@RequestMapping("/manage/managerApp")
public class ManagerAppManageController extends AbstractController {
    @Autowired
    private ManagerAppManageService managerAppManageService;
    @Autowired
    private ConfigService configService;

    /**
     * 添加管理员与应用关联
     *
     * @param managerCode 管理员编码（必须）
     * @param appCode     应用编码（必须）
     */
    @RequestMapping("/addManagerApp")
    public EmptyResult addManagerApp(String managerCode, String appCode) {
        assertAdmin();
        FindAppOrder findAppOrder = new FindAppOrder();
        findAppOrder.setAppCode(appCode);
        FindAppResult findAppResult = configService.findApp(findAppOrder);
        if (!findAppResult.isSuccess()) {
            throw new AntBekitException(Status.FAIL, findAppResult.getCode(), findAppResult.getMessage());
        }
        if (findAppResult.getAppInfo() == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("应用[%s]不存在", appCode));
        }
        AddManagerAppOrder order = new AddManagerAppOrder();
        order.setManagerCode(managerCode);
        order.setAppCode(appCode);

        return managerAppManageService.addManagerApp(order);
    }

    /**
     * 删除管理员与应用关联
     *
     * @param managerCode 管理员编码（必须）
     * @param appCode     应用编码（必须）
     */
    @RequestMapping("/deleteManagerApp")
    public DeleteManagerAppResult deleteManagerApp(String managerCode, String appCode) {
        assertAdmin();
        DeleteManagerAppOrder order = new DeleteManagerAppOrder();
        order.setManagerCode(managerCode);
        order.setAppCode(appCode);

        return managerAppManageService.deleteManagerApp(order);
    }

    /**
     * 查询被管理员管理的应用
     *
     * @param pageNo      页码（必须）
     * @param pageSize    每页大小（必须）
     * @param managerCode 管理员编码（必须）
     * @param appCode     应用编码
     */
    @RequestMapping("/queryManagedApp")
    public QueryManagedAppResult queryManagedApp(int pageNo, int pageSize, String managerCode, String appCode) {
        assertAdminOrMyself(managerCode);
        QueryManagedAppOrder order = new QueryManagedAppOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setManagerCode(managerCode);
        order.setAppCode(appCode);

        return managerAppManageService.queryManagedApp(order);
    }

    /**
     * 查询管理员与应用关联
     *
     * @param pageNo      页码（必须）
     * @param pageSize    每页大小（必须）
     * @param managerCode 管理员编码
     * @param appCode     应用编码
     */
    @RequestMapping("/queryManagerApp")
    public QueryManagerAppResult queryManagerApp(int pageNo, int pageSize, String managerCode, String appCode) {
        assertAdmin();
        QueryManagerAppOrder order = new QueryManagerAppOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setManagerCode(managerCode);
        order.setAppCode(appCode);

        return managerAppManageService.queryManagerApp(order);
    }
}
