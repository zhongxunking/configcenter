/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 21:35 创建
 */
package org.antframework.configcenter.web.manager.facade.info;


import org.antframework.configcenter.web.manager.facade.enums.ManagerType;

import java.io.Serializable;

/**
 * 管理员信息
 */
public class ManagerInfo implements Serializable {
    // 用户名
    private String userName;
    // 密码
    private String password;
    // 类型
    private ManagerType type;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
