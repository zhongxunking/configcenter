/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:25 创建
 */
package org.antframework.configcenter.facade.order.manage;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置多个属性value-order
 */
public class SetPropertyValuesOrder extends AbstractOrder {
    // 应用编码
    @NotBlank
    private String appCode;
    // 环境编码
    @NotBlank
    private String profileCode;
    // 需设置的多个value
    @Size(min = 1)
    @Valid
    private List<KeyValue> keyValues = new ArrayList<>();

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public List<KeyValue> getKeyValues() {
        return keyValues;
    }

    public void addKeyValue(KeyValue keyValue) {
        keyValues.add(keyValue);
    }

    /**
     * key-value对
     */
    public static class KeyValue {
        // key
        @NotBlank
        private String key;
        // value
        @NotBlank
        private String value;

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
    }
}
