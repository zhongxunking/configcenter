/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:43 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.BranchInfo;

/**
 * 查找分支result
 */
@Getter
@Setter
public class FindBranchResult extends AbstractResult {
    // 分支（null表示不存在该分支）
    private BranchInfo branch;
}
