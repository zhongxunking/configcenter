/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:47 创建
 */
package org.antframework.configcenter.facade.info;

import org.antframework.common.util.tostring.ToString;

import java.io.Serializable;

/**
 * 应用信息
 */
public class AppInfo implements Serializable {
    // 应用编码
    private String appCode;
    // 备注
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

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
