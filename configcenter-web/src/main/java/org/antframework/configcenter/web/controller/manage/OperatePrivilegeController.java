/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-28 22:36 创建
 */
package org.antframework.configcenter.web.controller.manage;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.OperatePrivilege;
import org.antframework.configcenter.web.common.OperatePrivileges;
import org.antframework.manager.web.CurrentManagerAssert;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作权限controller
 */
@RestController
@RequestMapping("/manage/operatePrivilege")
public class OperatePrivilegeController {
    /**
     * 添加或修改操作权限
     *
     * @param appId     应用id
     * @param keyRegex  配置key正则表达式
     * @param privilege 操作权限
     */
    @RequestMapping("/addOrModifyOperatePrivilege")
    public EmptyResult addOrModifyOperatePrivilege(String appId, String keyRegex, OperatePrivilege privilege) {
        CurrentManagerAssert.admin();
        OperatePrivileges.addOrModifyOperatePrivilege(appId, keyRegex, privilege);
        return FacadeUtils.buildSuccess(EmptyResult.class);
    }

    /**
     * 删除操作权限
     *
     * @param appId    应用id
     * @param keyRegex 配置key正则表达式
     */
    @RequestMapping("/deleteOperatePrivileges")
    public EmptyResult deleteOperatePrivileges(String appId, String keyRegex) {
        CurrentManagerAssert.admin();
        Assert.notNull(appId, "appId不能为空");
        Assert.notNull(keyRegex, "keyRegex不能为空");
        OperatePrivileges.deleteOperatePrivileges(appId, keyRegex);
        return FacadeUtils.buildSuccess(EmptyResult.class);
    }

    /**
     * 查找应用继承的操作权限
     *
     * @param appId 应用id
     * @return 由近及远应用继承的操作权限（该应用本身在第一位）
     */
    @RequestMapping("/findInheritedOperatePrivileges")
    public FindInheritedPrivilegesResult findInheritedOperatePrivileges(String appId) {
        ManagerApps.adminOrHaveApp(appId);
        FindInheritedPrivilegesResult result = FacadeUtils.buildSuccess(FindInheritedPrivilegesResult.class);
        result.setAppOperatePrivileges(OperatePrivileges.findInheritedOperatePrivileges(appId));
        return result;
    }

    /**
     * 查找应用继承的权限
     */
    @Getter
    @Setter
    public static class FindInheritedPrivilegesResult extends AbstractResult {
        // 由近及远应用继承的配置权限（该应用本身在第一位）
        private List<OperatePrivileges.AppOperatePrivilege> appOperatePrivileges;
    }
}
