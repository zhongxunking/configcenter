/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 20:16 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

import javax.validation.constraints.NotBlank;

/**
 * 查找环境继承的所有环境order
 */
@Getter
@Setter
public class FindInheritedProfilesOrder extends AbstractOrder {
    // 环境id
    @NotBlank
    private String profileId;
}
