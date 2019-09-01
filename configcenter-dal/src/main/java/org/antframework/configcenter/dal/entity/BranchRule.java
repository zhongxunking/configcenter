/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-01 14:20 创建
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
 * 分支规则
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_appId_profileId_branchId", columnNames = {"appId", "profileId", "branchId"}))
@Getter
@Setter
public class BranchRule extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // 环境id
    @Column(length = 64)
    private String profileId;

    // 分支id
    @Column(length = 64)
    private String branchId;

    // 规则
    @Column
    private String rule;

    // 优先级
    @Column
    private Long priority;
}
