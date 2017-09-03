/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:00 创建
 */
package org.antframework.configcenter.facade.result.manage;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ProfileInfo;

/**
 * 查找环境result
 */
public class FindProfileResult extends AbstractResult {
    // 环境信息
    private ProfileInfo profileInfo;

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }
}
