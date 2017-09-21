/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:37 创建
 */
package org.antframework.configcenter.dal.entity;

import org.antframework.boot.jpa.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 属性key
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"appCode", "key"}))
public class PropertyKey extends AbstractEntity {
    // 应用编码
    @Column(length = 64)
    private String appCode;

    // 属性key
    @Column(name = "`key`", length = 128)
    private String key;

    // 是否公开
    @Column
    private Boolean outward;

    // 备注
    @Column
    private String memo;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getOutward() {
        return outward;
    }

    public void setOutward(Boolean outward) {
        this.outward = outward;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
