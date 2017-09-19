/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 16:55 创建
 */
package org.antframework.configcenter.dal.entity.manage;

import org.antframework.boot.jpa.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 管理员管理的应用
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userName", "appCode"}))
public class ManagerApp extends AbstractEntity {
    // 用户名
    @Column(length = 64)
    private String userName;

    // 被管理的应用编码
    @Column(length = 64)
    private String appCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
