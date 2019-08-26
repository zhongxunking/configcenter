/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-26 22:50 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.ReleaseConstant;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 回滚分支的发布order
 */
@Getter
@Setter
public class RevertBranchReleaseOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
    // 环境id
    @NotBlank
    private String profileId;
    // 分支id
    @NotBlank
    private String branchId;
    // 回滚到的目标发布版本（传入ReleaseConstant.ORIGIN_VERSION表示删除所有发布）
    @Min(ReleaseConstant.ORIGIN_VERSION)
    @NotNull
    private Long targetReleaseVersion;
}
