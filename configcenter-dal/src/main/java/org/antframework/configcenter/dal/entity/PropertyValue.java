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

import javax.persistence.*;

/**
 * 配置value
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_appId_key_profileId", columnNames = {"appId", "key", "profileId"}),
        indexes = @Index(name = "idx_appId_profileId", columnList = "appId,profileId"))
@Getter
@Setter
public class PropertyValue extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // key
    @Column(name = "`key`", length = 128)
    private String key;

    // 环境id
    @Column(length = 64)
    private String profileId;

    // value
    @Column(length = 2048)
    @Mask(allMask = true)
    private String value;
}
