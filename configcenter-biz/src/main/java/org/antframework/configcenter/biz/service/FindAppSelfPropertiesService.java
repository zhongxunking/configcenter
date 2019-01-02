/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:31 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.biz.util.ReleaseUtils;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindAppSelfPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppSelfPropertiesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找应用自己的在指定环境中的配置服务
 */
@Service
public class FindAppSelfPropertiesService {

    @ServiceExecute
    public void execute(ServiceContext<FindAppSelfPropertiesOrder, FindAppSelfPropertiesResult> context) {
        FindAppSelfPropertiesOrder order = context.getOrder();
        FindAppSelfPropertiesResult result = context.getResult();
        // 获取每个继承的环境中的配置
        for (ProfileInfo profile : ProfileUtils.findInheritedProfiles(order.getProfileId())) {
            // 获取当前发布
            ReleaseInfo release = ReleaseUtils.findCurrentRelease(order.getAppId(), profile.getProfileId());
            // 移除作用域不合要求的配置
            release.getProperties().removeIf(property -> property.getScope().compareTo(order.getMinScope()) < 0);

            result.addInheritedRelease(release);
        }
    }
}
