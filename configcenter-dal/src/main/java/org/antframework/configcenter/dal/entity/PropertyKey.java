/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:37 创建
 */
package org.antframework.configcenter.dal.entity;

import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.Scope;

import javax.persistence.*;

/**
 * 属性key
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"appId", "key"}))
public class PropertyKey extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // 属性key
    @Column(name = "`key`", length = 128)
    private String key;

    // 作用域
    @Column
    @Enumerated(EnumType.STRING)
    private Scope scope;

    // 备注
    @Column
    private String memo;

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

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
