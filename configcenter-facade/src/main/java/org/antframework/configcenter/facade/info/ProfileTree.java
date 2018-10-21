/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 21:08 创建
 */
package org.antframework.configcenter.facade.info;

import org.antframework.common.util.tostring.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 环境树
 */
public class ProfileTree implements Serializable {
    // 根节点
    private ProfileInfo profile;
    // 子树
    private List<ProfileTree> children = new ArrayList<>();

    public ProfileTree(ProfileInfo profile) {
        this.profile = profile;
    }

    public ProfileInfo getProfile() {
        return profile;
    }

    public List<ProfileTree> getChildren() {
        return children;
    }

    public void addChild(ProfileTree child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
