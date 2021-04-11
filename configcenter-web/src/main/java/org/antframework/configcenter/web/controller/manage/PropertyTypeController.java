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
import org.antframework.configcenter.web.common.AppPropertyTypes;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.common.PropertyType;
import org.antframework.manager.web.CurrentManagerAssert;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作权限controller
 */
@RestController
@RequestMapping("/manage/propertyType")
public class PropertyTypeController {
    /**
     * 新增或修改规则
     *
     * @param appId        应用id
     * @param keyRegex     key正则表达式
     * @param propertyType 配置类型
     */
    @RequestMapping("/addOrModifyRule")
    public EmptyResult addOrModifyRule(String appId,
                                       String keyRegex,
                                       PropertyType propertyType,
                                       Integer priority) {
        Assert.notNull(appId, "appId不能为空");
        Assert.notNull(keyRegex, "keyRegex不能为null");
        Assert.notNull(propertyType, "propertyType不能为null");
        Assert.notNull(priority, "priority不能为null");
        CurrentManagerAssert.admin();

        AppPropertyTypes.addOrModifyRule(appId, new AppPropertyTypes.Rule(keyRegex, propertyType, priority));
        return FacadeUtils.buildSuccess(EmptyResult.class);
    }

    /**
     * 删除规则
     *
     * @param appId    应用id
     * @param keyRegex 规则的key正则表达式
     */
    @RequestMapping("/deleteRule")
    public EmptyResult deleteRule(String appId, String keyRegex) {
        Assert.notNull(appId, "appId不能为空");
        Assert.notNull(keyRegex, "keyRegex不能为空");
        CurrentManagerAssert.admin();
        AppPropertyTypes.deleteRule(appId, keyRegex);
        return FacadeUtils.buildSuccess(EmptyResult.class);
    }

    /**
     * 查找继承的应用规则
     *
     * @param appId 应用id
     */
    @RequestMapping("/findInheritedAppRules")
    public FindInheritedAppRulesResult findInheritedAppRules(String appId) {
        ManagerApps.assertAdminOrHaveApp(appId);
        FindInheritedAppRulesResult result = FacadeUtils.buildSuccess(FindInheritedAppRulesResult.class);
        result.setInheritedAppRules(AppPropertyTypes.findInheritedAppRules(appId));
        return result;
    }

    /**
     * 查找继承的应用规则-result
     */
    @Getter
    @Setter
    public static class FindInheritedAppRulesResult extends AbstractResult {
        // 由近及远继承的应用规则（该应用本身在第一位）
        private List<AppPropertyTypes.AppRule> inheritedAppRules;
    }
}
