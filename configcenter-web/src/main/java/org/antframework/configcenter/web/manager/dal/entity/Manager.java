/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 16:55 创建
 */
package org.antframework.configcenter.web.manager.dal.entity;

import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 管理员
 */
@Entity
public class Manager extends AbstractEntity {
    // 编码
    @Column(unique = true, length = 64)
    private String code;

    // 名称
    @Column(length = 64)
    private String name;

    // 密码
    @Column(length = 64)
    private String password;

    // 类型
    @Column(length = 40)
    @Enumerated(EnumType.STRING)
    private ManagerType type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ManagerType getType() {
        return type;
    }

    public void setType(ManagerType type) {
        this.type = type;
    }
}
