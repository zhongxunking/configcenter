/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 22:48 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

import javax.validation.constraints.NotBlank;

/**
 * 删除配置value-order
 */
@Getter
@Setter
public class DeletePropertyValueOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
    // 配置key
    @NotBlank
    private String key;
    // 环境id
    @NotBlank
    private String profileId;
}
