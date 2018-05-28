/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:53 创建
 */
package org.antframework.configcenter.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.Scope;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 添加或修改属性key-order
 */
public class AddOrModifyPropertyKeyOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
    // key
    @NotBlank
    private String key;
    // 作用域
    @NotNull
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
