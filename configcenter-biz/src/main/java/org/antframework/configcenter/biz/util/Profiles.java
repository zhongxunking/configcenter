/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-11 13:07 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.ProfileService;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ProfileTree;
import org.antframework.configcenter.facade.order.FindInheritedProfilesOrder;
import org.antframework.configcenter.facade.order.FindProfileOrder;
import org.antframework.configcenter.facade.order.FindProfileTreeOrder;
import org.antframework.configcenter.facade.order.QueryProfilesOrder;
import org.antframework.configcenter.facade.result.FindInheritedProfilesResult;
import org.antframework.configcenter.facade.result.FindProfileResult;
import org.antframework.configcenter.facade.result.FindProfileTreeResult;
import org.antframework.configcenter.facade.result.QueryProfilesResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 环境操作类
 */
public final class Profiles {
    // 分页查询环境使用的每页大小
    private static final int QUERY_PAGE_SIZE = 100;
    // 环境服务
    private static final ProfileService PROFILE_SERVICE = Contexts.getApplicationContext().getBean(ProfileService.class);

    /**
     * 查找环境
     *
     * @param profileId 环境id
     * @return 环境（null表示无该环境）
     */
    public static ProfileInfo findProfile(String profileId) {
        FindProfileOrder order = new FindProfileOrder();
        order.setProfileId(profileId);

        FindProfileResult result = PROFILE_SERVICE.findProfile(order);
        FacadeUtils.assertSuccess(result);
        return result.getProfile();
    }

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
     * 获取环境树
     *
     * @param rootProfileId 根节点环境id（null表示查找所有环境）
     * @return 环境树
     */
    public static ProfileTree findProfileTree(String rootProfileId) {
        FindProfileTreeOrder order = new FindProfileTreeOrder();
        order.setRootProfileId(rootProfileId);

        FindProfileTreeResult result = PROFILE_SERVICE.findProfileTree(order);
        FacadeUtils.assertSuccess(result);
        return result.getProfileTree();
    }

    /**
     * 查找所有环境
     *
     * @return 所有环境
     */
    public static List<ProfileInfo> findAllProfiles() {
        List<ProfileInfo> profiles = new ArrayList<>();

        int pageNo = 1;
        QueryProfilesResult result;
        do {
            result = PROFILE_SERVICE.queryProfiles(buildQueryProfilesOrder(pageNo++));
            FacadeUtils.assertSuccess(result);
            profiles.addAll(result.getInfos());
        } while (pageNo <= FacadeUtils.calcTotalPage(result.getTotalCount(), QUERY_PAGE_SIZE));

        return profiles;
    }

    // 构建查询环境的order
    private static QueryProfilesOrder buildQueryProfilesOrder(int pageNo) {
        QueryProfilesOrder order = new QueryProfilesOrder();
        order.setPageNo(pageNo);
        order.setPageSize(QUERY_PAGE_SIZE);
        order.setProfileId(null);
        order.setParent(null);

        return order;
    }
}
