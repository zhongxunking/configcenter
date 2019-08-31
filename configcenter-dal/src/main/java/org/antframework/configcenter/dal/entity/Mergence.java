/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-15 21:37 创建
 */
package org.antframework.configcenter.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;

import javax.persistence.*;

/**
 * 合并
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_appId_profileId_releaseVersion", columnNames = {"appId", "profileId", "releaseVersion"}),
        indexes = @Index(name = "idx_appId_profileId_sourceReleaseVersion", columnList = "appId,profileId,sourceReleaseVersion"))
@Getter
@Setter
public class Mergence extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // 环境id
    @Column(length = 64)
    private String profileId;

    // 发布版本
    @Column
    private Long releaseVersion;

    // 源发布版本
    @Column
    private Long sourceReleaseVersion;
}
