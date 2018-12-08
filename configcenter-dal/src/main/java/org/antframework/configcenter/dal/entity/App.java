/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:35 创建
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
 * 应用
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_appId", columnNames = "appId"))
@Getter
@Setter
public class App extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // 应用名
    @Column
    private String appName;

    // 发布版本
    @Column
    private Long releaseVersion;

    // 父应用id（null表示无父应用）
    @Column(length = 64)
    private String parent;
}
