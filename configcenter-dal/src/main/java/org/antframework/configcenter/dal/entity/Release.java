/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-04 22:17 创建
 */
package org.antframework.configcenter.dal.entity;

import org.antframework.boot.jpa.AbstractEntity;

import javax.persistence.*;

/**
 * 发布的配置
 */
@Entity(name = "`Release`")
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_appId_profileId_version", columnNames = {"appId", "profileId", "version"}),
        indexes = @Index(name = "idx_appId_profileId", columnList = "appId,profileId"))
public class Release extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // 环境id
    @Column(length = 64)
    private String profileId;

    // 版本
    @Column
    private Long version;

    // 配置项集合
    @Column(length = 1024 * 1024)
    private String properties;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
