/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:51 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.biz.util.Configs;
import org.antframework.configcenter.facade.info.AppRelease;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindConfigOrder;
import org.antframework.configcenter.facade.result.FindConfigResult;
import org.antframework.configcenter.facade.vo.Property;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 查找应用在指定环境中的配置服务
 */
@Service
public class FindConfigService {
    @ServiceExecute
    public void execute(ServiceContext<FindConfigOrder, FindConfigResult> context) {
        FindConfigOrder order = context.getOrder();
        FindConfigResult result = context.getResult();
        // 获取配置和版本
        long version = 0;
        Map<String, String> properties = new HashMap<>();
        // 根据应用继承关系获取
        List<AppRelease> inheritedAppReleases = Configs.findInheritedAppReleases(order.getMainAppId(), order.getQueriedAppId(), order.getProfileId(), order.getTarget());
        for (int i = inheritedAppReleases.size() - 1; i >= 0; i--) {
            // 根据环境继承关系获取
            List<ReleaseInfo> inheritedProfileReleases = inheritedAppReleases.get(i).getInheritedProfileReleases();
            for (int j = inheritedProfileReleases.size() - 1; j >= 0; j--) {
                // 获取发布中的配置
                ReleaseInfo release = inheritedProfileReleases.get(j);
                version += release.getVersion();
                for (Property property : release.getProperties()) {
                    properties.put(property.getKey(), property.getValue());
                }
            }
        }
        // 对配置排序
        Map<String, String> sortedProperties = new LinkedHashMap<>();
        properties.keySet().stream().sorted().forEach(key -> sortedProperties.put(key, properties.get(key)));

        result.setVersion(version);
        result.setProperties(sortedProperties);
    }
}
