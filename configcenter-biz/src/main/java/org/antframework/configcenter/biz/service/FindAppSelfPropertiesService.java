/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:31 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindAppSelfPropertiesOrder;
import org.antframework.configcenter.facade.order.FindCurrentReleaseOrder;
import org.antframework.configcenter.facade.result.FindAppSelfPropertiesResult;
import org.antframework.configcenter.facade.result.FindCurrentReleaseResult;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * 查找应用自己的在指定环境中的配置服务
 */
@Service
public class FindAppSelfPropertiesService {
    @Autowired
    private ReleaseService releaseService;

    @ServiceExecute
    public void execute(ServiceContext<FindAppSelfPropertiesOrder, FindAppSelfPropertiesResult> context) {
        FindAppSelfPropertiesOrder order = context.getOrder();
        FindAppSelfPropertiesResult result = context.getResult();
        // 获取每个继承的环境中的配置
        for (ProfileInfo profile : ProfileUtils.findInheritedProfiles(order.getProfileId())) {
            // 获取当前发布
            ReleaseInfo release = findCurrentRelease(order.getAppId(), profile.getProfileId());
            // 移除作用域不合要求的配置
            release.getProperties().removeIf(property -> property.getScope().compareTo(order.getMinScope()) < 0);

            result.addRelease(release);
        }
    }

    // 查找当前发布
    private ReleaseInfo findCurrentRelease(String appId, String profileId) {
        FindCurrentReleaseOrder order = new FindCurrentReleaseOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        FindCurrentReleaseResult result = releaseService.findCurrentRelease(order);
        FacadeUtils.assertSuccess(result);

        ReleaseInfo release = result.getRelease();
        if (release == null) {
            release = new ReleaseInfo();
            release.setAppId(appId);
            release.setProfileId(profileId);
            release.setVersion(ReleaseConstant.ORIGIN_VERSION);
            release.setMemo(null);
            release.setProperties(new ArrayList<>());
        }
        return release;
    }
}
