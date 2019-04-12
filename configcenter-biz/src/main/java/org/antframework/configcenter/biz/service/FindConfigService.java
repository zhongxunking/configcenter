/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:51 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.biz.util.Configs;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindConfigOrder;
import org.antframework.configcenter.facade.result.FindConfigResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 查找应用在指定环境中的配置服务
 */
@Service
public class FindConfigService {
    @ServiceExecute
    public void execute(ServiceContext<FindConfigOrder, FindConfigResult> context) {
        FindConfigOrder order = context.getOrder();
        FindConfigResult result = context.getResult();
        // 获取被查询配置的应用和主体应用继承的所有应用
        List<String> queriedAppIds = getInheritedAppIds(order.getQueriedAppId());
        Set<String> mainAppIds;
        if (Objects.equals(order.getMainAppId(), order.getQueriedAppId())) {
            mainAppIds = new HashSet<>(queriedAppIds);
        } else {
            mainAppIds = new HashSet<>(getInheritedAppIds(order.getMainAppId()));
        }
        // 获取应用的配置和版本
        AtomicLong version = new AtomicLong(0);
        Map<String, String> properties = new HashMap<>();
        for (String queriedAppId : queriedAppIds) {
            Map<String, String> temp = getAppSelfConfig(
                    queriedAppId,
                    order.getProfileId(),
                    calcMinScope(queriedAppId, order.getMainAppId(), mainAppIds),
                    version);
            temp.putAll(properties);
            properties = temp;
        }

        result.setVersion(version.get());
        result.setProperties(properties);
    }

    // 获取继承的所有应用
    private List<String> getInheritedAppIds(String appId) {
        List<String> appIds = new ArrayList<>();
        for (AppInfo app : Apps.findInheritedApps(appId)) {
            appIds.add(app.getAppId());
        }
        return appIds;
    }

    // 获取应用自己的配置
    private Map<String, String> getAppSelfConfig(String appId, String profileId, Scope minScope, AtomicLong version) {
        Map<String, String> properties = new HashMap<>();
        for (ReleaseInfo release : Configs.findAppSelfConfig(appId, profileId, minScope)) {
            version.addAndGet(release.getVersion());
            Map<String, String> temp = new HashMap<>();
            for (Property property : release.getProperties()) {
                temp.put(property.getKey(), property.getValue());
            }
            temp.putAll(properties);
            properties = temp;
        }
        return properties;
    }

    // 计算最小作用域
    private Scope calcMinScope(String queriedAppId, String mainAppId, Set<String> mainAppIds) {
        if (Objects.equals(queriedAppId, mainAppId)) {
            return Scope.PRIVATE;
        } else if (mainAppIds.contains(queriedAppId)) {
            return Scope.PROTECTED;
        } else {
            return Scope.PUBLIC;
        }
    }
}
