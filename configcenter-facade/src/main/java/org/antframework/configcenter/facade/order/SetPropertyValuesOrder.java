/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:25 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.common.util.tostring.ToString;
import org.antframework.common.util.tostring.format.Mask;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置多个配置value-order
 */
@Getter
public class SetPropertyValuesOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    @Setter
    private String appId;
    // 环境id
    @NotBlank
    @Setter
    private String profileId;
    // 配置key对应的value
    @Size(min = 1)
    @Valid
    private List<KeyValue> keyValues = new ArrayList<>();

    public void addKeyValue(KeyValue keyValue) {
        keyValues.add(keyValue);
    }

    /**
     * key-value对
     */
    @Getter
    @Setter
    public static class KeyValue implements Serializable {
        // key
        @NotBlank
        private String key;
        // value
        @Mask(allMask = true)
        private String value;

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }
}
