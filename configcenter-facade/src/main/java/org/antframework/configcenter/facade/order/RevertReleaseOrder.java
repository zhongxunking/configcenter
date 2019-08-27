/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-09 16:24 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.ReleaseConstant;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 回滚发布order
 */
@Getter
@Setter
public class RevertReleaseOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
    // 环境id
    @NotBlank
    private String profileId;
    // 需要回滚的源版本
    @Min(ReleaseConstant.ORIGIN_VERSION)
    @NotNull
    private Long sourceVersion;
    // 回滚截止的目标版本
    @Min(ReleaseConstant.ORIGIN_VERSION)
    @NotNull
    private Set<Long> targetVersions;
}
