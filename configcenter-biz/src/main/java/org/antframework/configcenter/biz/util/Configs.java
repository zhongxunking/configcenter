/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-20 23:51 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.info.AppRelease;
import org.antframework.configcenter.facade.order.FindInheritedAppReleasesOrder;
import org.antframework.configcenter.facade.result.FindInheritedAppReleasesResult;

import java.util.List;

/**
 * 配置操作类
 */
public final class Configs {
    // 配置服务
    private static final ConfigService CONFIG_SERVICE = Contexts.getApplicationContext().getBean(ConfigService.class);

    /**
     * 查找继承的应用发布
     *
     * @param mainAppId    主体应用id
     * @param queriedAppId 被查询配置的应用id
     * @param profileId    环境id
     * @param target       目标
     * @return 继承的应用发布
     */
    public static List<AppRelease> findInheritedAppReleases(String mainAppId, String queriedAppId, String profileId, String target) {
        FindInheritedAppReleasesOrder order = new FindInheritedAppReleasesOrder();
        order.setMainAppId(mainAppId);
        order.setQueriedAppId(queriedAppId);
        order.setProfileId(profileId);
        order.setTarget(target);

        FindInheritedAppReleasesResult result = CONFIG_SERVICE.findInheritedAppReleases(order);
        FacadeUtils.assertSuccess(result);
        return result.getInheritedAppReleases();
    }
}
