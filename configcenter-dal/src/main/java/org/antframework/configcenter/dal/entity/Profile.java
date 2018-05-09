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

    // 备注
    @Column
    private String memo;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
