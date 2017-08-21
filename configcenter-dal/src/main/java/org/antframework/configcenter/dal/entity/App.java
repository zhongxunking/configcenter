/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:35 创建
 */
package org.antframework.configcenter.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 应用
 */
@Entity
public class App extends AbstractEntity {
    // 应用编码
    @Column(unique = true, length = 64)
    private String appCode;

    // 备注
    @Column
    private String memo;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
