/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:51 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.AppService;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.FindAppSelfPropertiesOrder;
import org.antframework.configcenter.facade.order.FindInheritedAppsOrder;
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppSelfPropertiesResult;
import org.antframework.configcenter.facade.result.FindInheritedAppsResult;
import org.antframework.configcenter.facade.result.FindPropertiesResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.apache.commons.lang3.StringUtils;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 查找应用在特定环境中的配置服务
 */
@Service
public class FindPropertiesService {
    @Autowired
    private AppService appService;
    @Autowired
    private ConfigService configService;

    @ServiceExecute
    public void execute(ServiceContext<FindPropertiesOrder, FindPropertiesResult> context) {
        FindPropertiesOrder order = context.getOrder();
        FindPropertiesResult result = context.getResult();
        // 获取被查询配置的应用和主体应用继承的所有应用
        List<String> queriedAppIds = getInheritedApps(order.getQueriedAppId());
        Set<String> mainAppIds;
        if (StringUtils.equals(order.getMainAppId(), order.getQueriedAppId())) {
            mainAppIds = new HashSet<>(queriedAppIds);
        } else {
            mainAppIds = new HashSet<>(getInheritedApps(order.getMainAppId()));
        }
        // 获取应用的配置
        Map<String, String> properties = new HashMap<>();
        for (String queriedAppId : queriedAppIds) {
            Map<String, String> temp = getAppSelfProperties(queriedAppId, order.getProfileId(), calcMinScope(queriedAppId, order.getMainAppId(), mainAppIds));
            temp.putAll(properties);
            properties = temp;
        }

        result.setProperties(properties);
    }

    // 获取继承的所有应用
    private List<String> getInheritedApps(String appId) {
        FindInheritedAppsOrder order = new FindInheritedAppsOrder();
        order.setAppId(appId);

        FindInheritedAppsResult result = appService.findInheritedApps(order);
        if (!result.isSuccess()) {
            throw new BizException(Status.FAIL, result.getCode(), result.getMessage());
        }

        List<String> inheritedApps = new ArrayList<>();
        for (AppInfo app : result.getInheritedApps()) {
            inheritedApps.add(app.getAppId());
        }
        return inheritedApps;
    }

    // 获取应用自己的配置
    private Map<String, String> getAppSelfProperties(String appId, String profileId, Scope minScope) {
        FindAppSelfPropertiesOrder order = new FindAppSelfPropertiesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setMinScope(minScope);

        FindAppSelfPropertiesResult result = configService.findAppSelfProperties(order);
        if (!result.isSuccess()) {
            throw new BizException(Status.FAIL, result.getCode(), result.getMessage());
        }

        Map<String, String> properties = new HashMap<>();
        for (Property property : result.getProperties()) {
            properties.put(property.getKey(), property.getValue());
        }
        return properties;
    }

    // 计算最小作用域
    private Scope calcMinScope(String queriedAppId, String mainAppId, Set<String> mainAppIds) {
        if (StringUtils.equals(queriedAppId, mainAppId)) {
            return Scope.PRIVATE;
        } else if (mainAppIds.contains(queriedAppId)) {
            return Scope.PROTECTED;
        } else {
            return Scope.PUBLIC;
        }
    }
}
