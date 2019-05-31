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
import org.antframework.configcenter.web.common.KeyRegexPrivileges;
import org.antframework.configcenter.web.common.Privilege;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 配置key正则表达式对应的权限controller
 */
@RestController
@RequestMapping("/manage/keyRegexPrivilege")
public class KeyRegexPrivilegeController {
    /**
     * 添加或修改权限
     *
     * @param appId     应用id
     * @param keyRegex  key正则表达式
     * @param privilege 权限
     */
    @RequestMapping("/addOrModifyPrivilege")
    public EmptyResult addOrModifyPrivilege(String appId, String keyRegex, Privilege privilege) {
        KeyRegexPrivileges.addOrModifyPrivilege(appId, keyRegex, privilege);
        return FacadeUtils.buildSuccess(EmptyResult.class);
    }

    /**
     * 删除权限
     *
     * @param appId    应用id
     * @param keyRegex 配置key正则表达式
     */
    @RequestMapping("/deletePrivilege")
    public EmptyResult deletePrivilege(String appId, String keyRegex) {
        Assert.notNull(appId, "appId不能为空");
        Assert.notNull(keyRegex, "keyRegex不能为空");
        KeyRegexPrivileges.deletePrivileges(appId, keyRegex);
        return FacadeUtils.buildSuccess(EmptyResult.class);
    }

    /**
     * 查找应用继承的权限
     *
     * @param appId 应用id
     * @return 由近及远应用继承的配置权限（该应用本身在第一位）
     */
    @RequestMapping("/findInheritedPrivileges")
    public FindInheritedPrivilegesResult findInheritedPrivileges(String appId) {
        FindInheritedPrivilegesResult result = FacadeUtils.buildSuccess(FindInheritedPrivilegesResult.class);
        result.setAppPrivileges(KeyRegexPrivileges.findInheritedPrivileges(appId));
        return result;
    }

    /**
     * 查找应用继承的权限
     */
    @Getter
    @Setter
    public static class FindInheritedPrivilegesResult extends AbstractResult {
        // 由近及远应用继承的配置权限（该应用本身在第一位）
        private List<KeyRegexPrivileges.AppPrivilege> appPrivileges;
    }
}
