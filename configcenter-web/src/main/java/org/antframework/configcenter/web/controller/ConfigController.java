/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 16:11 创建
 */
package org.antframework.configcenter.web.controller;

import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindAppSelfPropertiesOrder;
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppSelfPropertiesResult;
import org.antframework.configcenter.facade.result.FindPropertiesResult;
import org.antframework.configcenter.facade.vo.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置controller
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    /**
     * 查找应用在特定环境中的配置
     *
     * @param mainAppId    主体应用id（必须）
     * @param queriedAppId 被查询配置的应用id（必须）
     * @param profileId    环境id（必须）
     */
    @RequestMapping("/findProperties")
    public FindPropertiesResult findProperties(String mainAppId, String queriedAppId, String profileId) {
        FindPropertiesOrder order = new FindPropertiesOrder();
        order.setMainAppId(mainAppId);
        order.setQueriedAppId(queriedAppId);
        order.setProfileId(profileId);

        return configService.findProperties(order);
    }

    /**
     * 查找应用自己的在特定环境中的配置
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param minScope  最小作用域
     */
    @RequestMapping("/findAppSelfProperties")
    public FindAppSelfPropertiesResult findAppSelfProperties(String appId, String profileId, Scope minScope) {
        FindAppSelfPropertiesOrder order = new FindAppSelfPropertiesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setMinScope(minScope);

        return configService.findAppSelfProperties(order);
    }
}
