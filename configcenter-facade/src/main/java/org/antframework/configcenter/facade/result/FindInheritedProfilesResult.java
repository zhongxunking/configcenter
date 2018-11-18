/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 20:18 创建
 */
package org.antframework.configcenter.facade.result;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ProfileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找环境继承的所有环境result
 */
public class FindInheritedProfilesResult extends AbstractResult {
    // 由近及远继承的所用环境（该环境本身在第一位）
    private List<ProfileInfo> inheritedProfiles = new ArrayList<>();

    public List<ProfileInfo> getInheritedProfiles() {
        return inheritedProfiles;
    }

    public void addInheritedProfile(ProfileInfo inheritedProfile) {
        inheritedProfiles.add(inheritedProfile);
    }
}
