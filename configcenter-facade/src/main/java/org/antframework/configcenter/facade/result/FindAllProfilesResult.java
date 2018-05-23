/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 20:18 创建
 */
package org.antframework.configcenter.facade.result;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ProfileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找所有环境result
 */
public class FindAllProfilesResult extends AbstractResult {
    // 所有环境
    private List<ProfileInfo> profiles = new ArrayList<>();

    public List<ProfileInfo> getProfiles() {
        return profiles;
    }

    public void addProfile(ProfileInfo profile) {
        profiles.add(profile);
    }
}
