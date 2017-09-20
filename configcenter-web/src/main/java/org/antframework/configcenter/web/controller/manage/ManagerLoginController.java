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
import org.antframework.configcenter.web.common.SessionAccessor;
import org.antframework.configcenter.web.manager.facade.api.ManagerManageService;
import org.antframework.configcenter.web.manager.facade.order.ManagerLoginOrder;
import org.antframework.configcenter.web.manager.facade.result.ManagerLoginResult;
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
     * @param username 用户名（必填）
     * @param password 密码（必填）
     */
    @RequestMapping("/login")
    public AbstractResult login(String username, String password) {
        ManagerLoginOrder order = new ManagerLoginOrder();
        order.setUsername(username);
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
}
