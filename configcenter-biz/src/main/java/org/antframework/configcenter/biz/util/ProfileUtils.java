/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-11 13:07 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.ProfileService;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.FindInheritedProfilesOrder;
import org.antframework.configcenter.facade.result.FindAllProfilesResult;
import org.antframework.configcenter.facade.result.FindInheritedProfilesResult;

import java.util.List;

/**
 * 环境工具类
 */
public final class ProfileUtils {
    // 环境服务
    private static final ProfileService PROFILE_SERVICE = Contexts.getApplicationContext().getBean(ProfileService.class);

    /**
     * 查找环境继承的所有环境
     *
     * @param profileId 被查找的环境id
     * @return 由近及远继承的所有环境
     */
    public static List<ProfileInfo> findInheritedProfiles(String profileId) {
        FindInheritedProfilesOrder order = new FindInheritedProfilesOrder();
        order.setProfileId(profileId);

        FindInheritedProfilesResult result = PROFILE_SERVICE.findInheritedProfiles(order);
        FacadeUtils.assertSuccess(result);
        return result.getInheritedProfiles();
    }

    /**
     * 查找所有环境
     *
     * @return 所有环境
     */
    public static List<ProfileInfo> findAllProfiles() {
        FindAllProfilesResult result = PROFILE_SERVICE.findAllProfiles(new EmptyOrder());
        FacadeUtils.assertSuccess(result);
        return result.getProfiles();
    }
}
