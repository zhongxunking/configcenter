/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:41 创建
 */
package org.antframework.configcenter.dal.entity;

import org.antframework.boot.jpa.AbstractEntity;

import javax.persistence.*;

/**
 * 属性value
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"appId", "key", "profileId"}), indexes = @Index(columnList = "appId,profileId"))
public class PropertyValue extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // 属性key
    @Column(name = "`key`", length = 128)
    private String key;

    // 环境id
    @Column(length = 64)
    private String profileId;

    // 属性value
    @Column
    private String value;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
