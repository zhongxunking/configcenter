/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-04 12:02 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

import javax.validation.constraints.NotBlank;

/**
 * 查找继承的应用配置key-order
 */
@Getter
@Setter
public class FindInheritedAppPropertyKeysOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
}
