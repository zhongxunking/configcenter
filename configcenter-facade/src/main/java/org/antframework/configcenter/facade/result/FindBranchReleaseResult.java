/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-23 21:02 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ReleaseInfo;

/**
 * 查找分支发布result
 */
@Getter
@Setter
public class FindBranchReleaseResult extends AbstractResult {
    // 分支发布
    private ReleaseInfo release;
}
