/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:53 创建
 */
package org.antframework.configcenter.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 添加或删除属性key-order
 */
public class AddOrModifyPropertyKeyOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
    // key
    @NotBlank
    private String key;
    // 是否公开
    private boolean outward;
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
