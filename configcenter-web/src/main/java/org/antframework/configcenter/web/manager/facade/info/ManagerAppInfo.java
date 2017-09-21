/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 21:59 创建
 */
package org.antframework.configcenter.web.manager.facade.info;

import org.antframework.common.util.tostring.ToString;

import java.io.Serializable;

/**
 * 管理员与应用关联信息
 */
public class ManagerAppInfo implements Serializable {
    // 管理员编码
    private String managerCode;
    // 被管理的应用编码
    private String appCode;

    public String getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(String managerCode) {
        this.managerCode = managerCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
