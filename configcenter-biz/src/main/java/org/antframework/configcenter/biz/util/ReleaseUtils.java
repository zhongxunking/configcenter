/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-10 21:04 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.order.RevertReleaseOrder;
import org.antframework.configcenter.facade.vo.ReleaseConstant;

/**
 * 发布工具类
 */
public final class ReleaseUtils {
    // 发布服务
    private static final ReleaseService RELEASE_SERVICE = Contexts.getApplicationContext().getBean(ReleaseService.class);

    /**
     * 删除应用在指定环境的所有发布
     *
     * @param appId     应用id
     * @param profileId 环境id
     */
    public static void deleteAppProfileReleases(String appId, String profileId) {
        RevertReleaseOrder order = new RevertReleaseOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setVersion(ReleaseConstant.ORIGIN_VERSION);

        EmptyResult result = RELEASE_SERVICE.revertRelease(order);
        FacadeUtils.assertSuccess(result);
    }
}
