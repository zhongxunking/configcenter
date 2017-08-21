/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:41 创建
 */
package org.antframework.configcenter.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 属性value
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"profileCode", "appCode", "key"}))
public class PropertyValue {
    // 环境编码
    @Column(length = 64)
    private String profileCode;

    // 应用编码
    @Column(length = 64)
    private String appCode;

    // 属性key
    @Column(name = "`key`", length = 128)
    private String key;

    // 属性value
    @Column
    private String value;

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
