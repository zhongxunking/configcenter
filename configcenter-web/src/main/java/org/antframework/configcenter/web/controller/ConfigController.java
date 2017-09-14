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
 *
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @RequestMapping("/queryProperties")
    public QueryPropertiesResult queryProperties(String appCode, String queriedAppCode, String profileCode) {
        FindAppResult findAppResult = configService.findApp(buildFindAppOrder(appCode));
        if (!findAppResult.isSuccess() || findAppResult.getAppInfo() == null) {
            throw new AntBekitException(Status.FAIL, findAppResult.getCode(), findAppResult.getMessage());
        }
        QueryPropertiesResult queryPropertiesResult = configService.queryProperties(buildQueryPropertiesOrder(queriedAppCode, profileCode, appCode));
        return queryPropertiesResult;
    }

    private FindAppOrder buildFindAppOrder(String appCode) {
        FindAppOrder order = new FindAppOrder();
        order.setAppCode(appCode);
        return order;
    }

    private QueryPropertiesOrder buildQueryPropertiesOrder(String queriedAppCode, String profileCode, String appCode) {
        QueryPropertiesOrder order = new QueryPropertiesOrder();
        order.setAppCode(queriedAppCode);
        order.setProfileCode(profileCode);
        order.setOnlyCommon(!StringUtils.equals(queriedAppCode, appCode));
        return order;
    }
}
