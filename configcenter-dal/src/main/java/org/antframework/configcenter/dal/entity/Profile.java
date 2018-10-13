/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:34 创建
 */
package org.antframework.configcenter.dal.entity;

import org.antframework.boot.jpa.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 环境
 */
@Entity
public class Profile extends AbstractEntity {
    // 环境id
    @Column(unique = true, length = 64)
    private String profileId;

    // 环境名
    @Column
    private String profileName;

    // 父环境id（null表示无父环境）
    @Column
    private String parent;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
