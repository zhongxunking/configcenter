/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-10 21:04 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindReleaseOrder;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.vo.ReleaseConstant;

import java.util.Date;
import java.util.HashSet;

/**
 * 发布操作类
 */
public final class Releases {
    // 发布服务
    private static final ReleaseService RELEASE_SERVICE = Contexts.getApplicationContext().getBean(ReleaseService.class);

    /**
     * 查找发布
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param version   版本
     * @return 发布（null表示无该发布）
     */
    public static ReleaseInfo findRelease(String appId, String profileId, long version) {
        FindReleaseOrder order = new FindReleaseOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setVersion(version);

        FindReleaseResult result = RELEASE_SERVICE.findRelease(order);
        FacadeUtils.assertSuccess(result);
        return result.getRelease();
    }

    /**
     * 构建原始发布
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @return 原始发布
     */
    public static ReleaseInfo buildOriginRelease(String appId, String profileId) {
        ReleaseInfo release = new ReleaseInfo();
        release.setAppId(appId);
        release.setProfileId(profileId);
        release.setVersion(ReleaseConstant.ORIGIN_VERSION);
        release.setReleaseTime(new Date());
        release.setMemo("原始发布");
        release.setProperties(new HashSet<>());
        release.setParentVersion(null);

        return release;
    }
}
