/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 19:59 创建
 */
package org.antframework.configcenter.facade.result;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ProfileInfo;

/**
 * 查找环境result
 */
public class FindProfileResult extends AbstractResult {
    // 环境（null表示无该环境）
    private ProfileInfo profile;

    public ProfileInfo getProfile() {
        return profile;
    }

    public void setProfile(ProfileInfo profile) {
        this.profile = profile;
    }
}
