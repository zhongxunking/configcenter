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
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.common.ResultCode;
import org.antframework.configcenter.web.common.SessionAccessor;
import org.antframework.configcenter.web.manager.facade.api.ManagerManageService;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;
import org.antframework.configcenter.web.manager.facade.info.ManagerInfo;
import org.antframework.configcenter.web.manager.facade.order.AddManagerOrder;
import org.antframework.configcenter.web.manager.facade.order.ManagerLoginOrder;
import org.antframework.configcenter.web.manager.facade.order.QueryManagerOrder;
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
public class ManagerLoginController extends AbstractController {
    @Autowired
    private ManagerManageService managerManageService;

    /**
     * 登陆
     *
     * @param managerCode 管理员编码（必填）
     * @param password    密码（必填）
     */
    @RequestMapping("/login")
    public ManagerLoginResult login(String managerCode, String password) {
        ManagerLoginOrder order = new ManagerLoginOrder();
        order.setManagerCode(managerCode);
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
    public EmptyResult logout() {
        SessionAccessor.removeManagerInfo();
        return buildSuccessResult();
    }

    /**
     * 是否应该初始化超级管理员
     */
    @RequestMapping("/shouldInitAdmin")
    public EmptyResult shouldInitAdmin() {
        assertInitAdmin();
        return buildSuccessResult();
    }

    /**
     * 初始化超级管理员
     *
     * @param managerCode 管理员编码（必填）
     * @param name        名称（必填）
     * @param password    密码（必填）
     */
    @RequestMapping("/initAdmin")
    public EmptyResult initAdmin(String managerCode, String name, String password) {
        assertInitAdmin();
        AddManagerOrder order = new AddManagerOrder();
        order.setManagerCode(managerCode);
        order.setName(name);
        order.setPassword(password);
        order.setType(ManagerType.ADMIN);

        return managerManageService.addManager(order);
    }

    /**
     * 获取已经登陆的管理员
     */
    @RequestMapping("/getLoginedManager")
    public FindLoginedManagerResult getLoginedManager() {
        FindLoginedManagerResult result = new FindLoginedManagerResult();
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
        result.setManagerInfo(SessionAccessor.getManagerInfo());

        return result;
    }

    // 断言是否能初始化超级管理员
    private void assertInitAdmin() {
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

    // 查找登陆的管理员result
    private static class FindLoginedManagerResult extends AbstractResult {
        // 管理员info
        private ManagerInfo managerInfo;

        public ManagerInfo getManagerInfo() {
            return managerInfo;
        }

        public void setManagerInfo(ManagerInfo managerInfo) {
            this.managerInfo = managerInfo;
        }
    }
}
