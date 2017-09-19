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
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/manage/login")
public class LoginController {
    @Autowired
    private ManagerDao managerDao;

    @RequestMapping("/login")
    public AbstractResult login(String username, String password) {
        Manager manager = managerDao.findByUserName(username);
        if (manager == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.UNKNOWN_ERROR.getCode(), String.format("用户[%s]不存在", username));
        }
        if (!StringUtils.equals(password, manager.getPassword())) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.UNKNOWN_ERROR.getCode(), "密码不正确");
        }
        SessionAccessor.setManager(manager);
        throw new AntBekitException(Status.SUCCESS, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
    }

    @RequestMapping("/logout")
    public AbstractResult logout() {
        SessionAccessor.removeManager();
        throw new AntBekitException(Status.SUCCESS, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
    }
}
