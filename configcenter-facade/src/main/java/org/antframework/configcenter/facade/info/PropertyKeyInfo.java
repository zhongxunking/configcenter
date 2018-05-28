/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-02 13:03 创建
 */
package org.antframework.configcenter.facade.info;

import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.Scope;

/**
 * 属性key-info
 */
public class PropertyKeyInfo extends AbstractInfo {
    // 应用id
    private String appId;
    // 属性key
    private String key;
    // 作用域
    private Scope scope;
    // 备注
    private String memo;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
