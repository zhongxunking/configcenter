/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-05 23:31 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.MergenceDifference;

/**
 * 计算分支合并result
 */
@Getter
@Setter
public class ComputeBranchMergenceResult extends AbstractResult {
    // 需合并的差异
    private MergenceDifference difference;
}
