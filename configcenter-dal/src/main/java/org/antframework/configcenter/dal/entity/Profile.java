/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:34 创建
 */
package org.antframework.configcenter.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 环境
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_profileId", columnNames = "profileId"))
@Getter
@Setter
public class Profile extends AbstractEntity {
    // 环境id
    @Column(length = 64)
    private String profileId;

    // 环境名
    @Column
    private String profileName;

    // 父环境id（null表示无父环境）
    @Column(length = 64)
    private String parent;
}
