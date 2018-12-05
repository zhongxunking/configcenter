/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:37 创建
 */
package org.antframework.configcenter.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.Scope;

import javax.persistence.*;

/**
 * 配置key
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_appId_key", columnNames = {"appId", "key"}))
@Getter
@Setter
public class PropertyKey extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // key
    @Column(name = "`key`", length = 128)
    private String key;

    // 作用域
    @Column(length = 64)
    @Enumerated(EnumType.STRING)
    private Scope scope;

    // 备注
    @Column
    private String memo;
}
