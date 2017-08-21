/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:34 创建
 */
package org.antframework.configcenter.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 环境
 */
@Entity
public class Profile extends AbstractEntity {
    // 环境编码
    @Column(unique = true, length = 64)
    private String profileCode;

    // 备注
    @Column
    private String memo;

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
