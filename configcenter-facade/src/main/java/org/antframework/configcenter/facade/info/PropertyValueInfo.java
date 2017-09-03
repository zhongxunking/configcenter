/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-02 19:37 创建
 */
package org.antframework.configcenter.facade.info;

import org.antframework.common.util.tostring.ToString;

import java.io.Serializable;

/**
 * 属性value信息
 */
public class PropertyValueInfo implements Serializable {
    // 环境编码
    private String profileCode;
    // 应用编码
    private String appCode;
    // 属性key
    private String key;
    // 属性value
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

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
