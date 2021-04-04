/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-04 14:39 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

import javax.validation.constraints.NotBlank;

/**
 * 查找继承的应用发布-order
 */
@Getter
@Setter
public class FindInheritedAppReleasesOrder extends AbstractOrder {
    // 主体应用id
    @NotBlank
    private String mainAppId;
    // 被查询配置的应用id
    @NotBlank
    private String queriedAppId;
    // 环境id
    @NotBlank
    private String profileId;
    // 目标
    private String target;
}
