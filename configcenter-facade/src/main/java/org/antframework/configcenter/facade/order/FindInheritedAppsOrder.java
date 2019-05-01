/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-22 23:01 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

import javax.validation.constraints.NotBlank;

/**
 * 查找应用继承的所有应用order
 */
@Getter
@Setter
public class FindInheritedAppsOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
}
