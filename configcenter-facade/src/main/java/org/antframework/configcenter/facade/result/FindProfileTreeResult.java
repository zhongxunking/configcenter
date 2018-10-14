/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 21:16 创建
 */
package org.antframework.configcenter.facade.result;

import org.antframework.configcenter.facade.info.ProfileTree;

/**
 * 查找环境树result
 */
public class FindProfileTreeResult {
    // 环境树
    private ProfileTree profileTree;

    public ProfileTree getProfileTree() {
        return profileTree;
    }

    public void setProfileTree(ProfileTree profileTree) {
        this.profileTree = profileTree;
    }
}
