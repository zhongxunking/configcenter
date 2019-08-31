/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-01 00:42 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.BranchInfo;

/**
 * 发布分支result
 */
@Getter
@Setter
public class ReleaseBranchResult extends AbstractResult {
    // 发布后的分支
    private BranchInfo branch;
}
