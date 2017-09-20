/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 22:08 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.common.ResultCode;
import org.antframework.configcenter.web.common.SessionAccessor;
import org.antframework.configcenter.web.manager.facade.api.ManagerManageService;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;
import org.antframework.configcenter.web.manager.facade.order.AddManagerOrder;
import org.antframework.configcenter.web.manager.facade.order.ManagerLoginOrder;
import org.antframework.configcenter.web.manager.facade.order.QueryManagerOrder;
import org.antframework.configcenter.web.manager.facade.result.AddManagerResult;
import org.antframework.configcenter.web.manager.facade.result.ManagerLoginResult;
import org.antframework.configcenter.web.manager.facade.result.QueryManagerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员登陆controller
 */
@RestController
@RequestMapping("/manage/managerLogin")
public class ManagerLoginController {
    @Autowired
    private ManagerManageService managerManageService;

    /**
     * 登陆
     *
     * @param code     编码（必填）
     * @param password 密码（必填）
     */
    @RequestMapping("/login")
    public ManagerLoginResult login(String code, String password) {
        ManagerLoginOrder order = new ManagerLoginOrder();
        order.setCode(code);
        order.setPassword(password);

        ManagerLoginResult result = managerManageService.managerLogin(order);
        if (result.isSuccess()) {
            SessionAccessor.setManagerInfo(result.getManagerInfo());
        }
        return result;
    }

    /**
     * 退出
     */
    @RequestMapping("/logout")
    public AbstractResult logout() {
        SessionAccessor.removeManagerInfo();
        throw new AntBekitException(Status.SUCCESS, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
    }

    @RequestMapping("/shouldInitAdmin")
    public AbstractResult shouldInitAdmin() {
        canInitAdmin();
        throw new AntBekitException(Status.SUCCESS, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
    }

    /**
     * 初始化超级管理员
     *
     * @param code     编码
     * @param name     名称
     * @param password 密码
     */
    @RequestMapping("/initAdmin")
    public AddManagerResult initAdmin(String code, String name, String password) {
        canInitAdmin();
        AddManagerOrder order = new AddManagerOrder();
        order.setCode(code);
        order.setName(name);
        order.setPassword(password);
        order.setType(ManagerType.ADMIN);

        return managerManageService.addManager(order);
    }

    private void canInitAdmin() {
        QueryManagerOrder order = new QueryManagerOrder();
        order.setPageNo(1);
        order.setPageSize(1);
        QueryManagerResult result = managerManageService.queryManager(order);
        if (!result.isSuccess()) {
            throw new AntBekitException(Status.FAIL, result.getCode(), result.getMessage());
        }
        if (result.getTotalCount() > 0) {
            throw new AntBekitException(Status.FAIL, ResultCode.NO_PERMISSION.getCode(), "已存在管理员，不能初始化超级管理员");
        }
    }
}
