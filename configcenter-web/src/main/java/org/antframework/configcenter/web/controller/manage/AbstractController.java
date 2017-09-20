/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 18:06 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.common.ResultCode;
import org.antframework.configcenter.web.common.SessionAccessor;
import org.antframework.configcenter.web.manager.facade.api.ManagerAppManageService;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;
import org.antframework.configcenter.web.manager.facade.order.FindManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.result.FindManagerAppResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public abstract class AbstractController {
    @Autowired
    private ManagerAppManageService managerAppManageService;

    public void assertLogined() {
        if (SessionAccessor.getManagerInfo() == null) {
            throw new AntBekitException(Status.FAIL, ResultCode.NOT_LOGIN.getCode(), ResultCode.NOT_LOGIN.getMessage());
        }
    }

    public void assertAdmin() {
        assertLogined();
        if (SessionAccessor.getManagerInfo().getType() != ManagerType.ADMIN) {
            throw new AntBekitException(Status.FAIL, ResultCode.NO_PERMISSION.getCode(), SessionAccessor.getManagerInfo().getCode() + "不是超级管理员");
        }
    }

    public void assertAdminOrMyself(String code) {
        assertLogined();
        if (SessionAccessor.getManagerInfo().getType() != ManagerType.ADMIN
                && StringUtils.equals(SessionAccessor.getManagerInfo().getCode(), code)) {
            throw new AntBekitException(Status.FAIL, ResultCode.NO_PERMISSION.getCode(), ResultCode.NO_PERMISSION.getMessage());
        }
    }

    public void canModifyApp(String appCode) {
        assertLogined();
        if (SessionAccessor.getManagerInfo().getType() == ManagerType.ADMIN) {
            return;
        }
        FindManagerAppOrder order = new FindManagerAppOrder();
        order.setManagerCode(SessionAccessor.getManagerInfo().getCode());
        order.setAppCode(appCode);

        FindManagerAppResult result = managerAppManageService.findManagerApp(order);
        if (!result.isSuccess()) {
            throw new AntBekitException(Status.FAIL, result.getCode(), result.getMessage());
        }
        if (result.getInfo() == null) {
            throw new AntBekitException(Status.FAIL, ResultCode.NO_PERMISSION.getCode(), String.format("管理员[%s]无权限操作应用[%s]", SessionAccessor.getManagerInfo().getCode(), appCode));
        }
    }
}
