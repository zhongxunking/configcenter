/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:41 创建
 */
package org.antframework.configcenter.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.common.util.tostring.format.Mask;
import org.antframework.configcenter.facade.vo.Scope;

import javax.persistence.*;

/**
 * 配置value
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_appId_profileId_branchId_key", columnNames = {"appId", "profileId", "branchId", "key"}))
@Getter
@Setter
public class PropertyValue extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // 环境id
    @Column(length = 64)
    private String profileId;

    // 分支id
    @Column(length = 64)
    private String branchId;

    // key
    @Column(name = "`key`", length = 128)
    private String key;

    // value
    @Column
    @Lob
    @Mask(allMask = true)
    private String value;

    // 作用域
    @Column(length = 64)
    @Enumerated(EnumType.STRING)
    private Scope scope;
}
