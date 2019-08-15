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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 合并
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_appId_profileId_releaseVersion", columnNames = {"appId", "profileId", "releaseVersion"}))
@Getter
@Setter
public class Mergence {
    // 应用id
    @Column(length = 64)
    private String appId;

    // 环境id
    @Column(length = 64)
    private String profileId;

    // 发布版本
    @Column
    private Long releaseVersion;

    // 被合并的发布版本
    @Column
    private Long mergedReleaseVersion;
}
