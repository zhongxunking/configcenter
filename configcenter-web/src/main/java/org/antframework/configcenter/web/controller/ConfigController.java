/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 16:11 创建
 */
package org.antframework.configcenter.web.controller;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.order.QueryPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.QueryPropertiesResult;
import org.apache.commons.lang3.StringUtils;
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
     * 查询应用的配置属性
     *
     * @param appCode        应用编码
     * @param queriedAppCode 被查询配置的应用编码
     * @param profileCode    环境编码
     */
    @RequestMapping("/queryProperties")
    public QueryPropertiesResult queryProperties(String appCode, String queriedAppCode, String profileCode) {
        FindAppResult findAppResult = configService.findApp(buildFindAppOrder(appCode));
        if (!findAppResult.isSuccess() || findAppResult.getAppInfo() == null) {
            throw new AntBekitException(Status.FAIL, findAppResult.getCode(), findAppResult.getMessage());
        }
        QueryPropertiesResult queryPropertiesResult = configService.queryProperties(buildQueryPropertiesOrder(queriedAppCode, profileCode, appCode));
        return queryPropertiesResult;
    }

    // 构建FindAppOrder
    private FindAppOrder buildFindAppOrder(String appCode) {
        FindAppOrder order = new FindAppOrder();
        order.setAppCode(appCode);
        return order;
    }

    // 构建QueryPropertiesOrder
    private QueryPropertiesOrder buildQueryPropertiesOrder(String queriedAppCode, String profileCode, String appCode) {
        QueryPropertiesOrder order = new QueryPropertiesOrder();
        order.setAppCode(queriedAppCode);
        order.setProfileCode(profileCode);
        order.setOnlyOutward(!StringUtils.equals(queriedAppCode, appCode));
        return order;
    }
}
