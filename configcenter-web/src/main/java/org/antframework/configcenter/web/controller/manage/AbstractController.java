/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 18:06 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.web.common.ResultCode;
import org.antframework.configcenter.web.common.SessionAccessor;
import org.antframework.configcenter.web.manager.facade.api.ManagerAppManageService;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;
import org.antframework.configcenter.web.manager.facade.order.FindManagerAppOrder;
import org.antframework.configcenter.web.manager.facade.result.FindManagerAppResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象controller
 */
public abstract class AbstractController {
    @Autowired
    private ManagerAppManageService managerAppManageService;

    // 构建成功result
    protected EmptyResult buildSuccessResult() {
        EmptyResult result = new EmptyResult();
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());

        return result;
    }

    // 确保已经登陆了
    public void assertLogined() {
        if (SessionAccessor.getManagerInfo() == null) {
            throw new AntBekitException(Status.FAIL, ResultCode.NOT_LOGIN.getCode(), ResultCode.NOT_LOGIN.getMessage());
        }
    }

    // 确保是超级管理员
    public void assertAdmin() {
        assertLogined();
        if (SessionAccessor.getManagerInfo().getType() != ManagerType.ADMIN) {
            throw new AntBekitException(Status.FAIL, ResultCode.NO_PERMISSION.getCode(), ResultCode.NO_PERMISSION.getMessage());
        }
    }

    // 确保是超级管理员或自己
    public void assertAdminOrMyself(String managerCode) {
        assertLogined();
        if (SessionAccessor.getManagerInfo().getType() != ManagerType.ADMIN
                && !StringUtils.equals(SessionAccessor.getManagerInfo().getManagerCode(), managerCode)) {
            throw new AntBekitException(Status.FAIL, ResultCode.NO_PERMISSION.getCode(), ResultCode.NO_PERMISSION.getMessage());
        }
    }

    // 本管理员是否能读取应用
    protected void canReadApp(String appCode) {
        assertLogined();
        if (StringUtils.equals(appCode, ConfigService.COMMON_APP_CODE)) {
            return;
        }
        if (SessionAccessor.getManagerInfo().getType() == ManagerType.ADMIN) {
            return;
        }
        assertHaveManagerApp(SessionAccessor.getManagerInfo().getManagerCode(), appCode);
    }

    // 本管理员是否能修改应用
    public void canModifyApp(String appCode) {
        assertLogined();
        if (SessionAccessor.getManagerInfo().getType() == ManagerType.ADMIN) {
            return;
        }
        assertHaveManagerApp(SessionAccessor.getManagerInfo().getManagerCode(), appCode);
    }

    // 断定管理员和应用有关联
    protected void assertHaveManagerApp(String managerCode, String appCode) {
        FindManagerAppOrder order = new FindManagerAppOrder();
        order.setManagerCode(managerCode);
        order.setAppCode(appCode);

        FindManagerAppResult result = managerAppManageService.findManagerApp(order);
        if (!result.isSuccess()) {
            throw new AntBekitException(Status.FAIL, result.getCode(), result.getMessage());
        }
        if (result.getInfo() == null) {
            throw new AntBekitException(Status.FAIL, ResultCode.NO_PERMISSION.getCode(), ResultCode.NO_PERMISSION.getMessage());
        }
    }
}
