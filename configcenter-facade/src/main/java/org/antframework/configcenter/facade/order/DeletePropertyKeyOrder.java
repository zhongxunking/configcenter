/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:56 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 删除配置key-order
 */
@Getter
@Setter
public class DeletePropertyKeyOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
    // key
    @NotBlank
    private String key;
}
