/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-02 13:03 创建
 */
package org.antframework.configcenter.facade.info;

import org.antframework.common.util.facade.AbstractInfo;

/**
 * 属性key信息
 */
public class PropertyKeyInfo extends AbstractInfo {
    // 应用编码
    private String appCode;
    // 属性key
    private String key;
    // 是否公开
    private boolean outward;
    // 备注
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

    public boolean isOutward() {
        return outward;
    }

    public void setOutward(boolean outward) {
        this.outward = outward;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
