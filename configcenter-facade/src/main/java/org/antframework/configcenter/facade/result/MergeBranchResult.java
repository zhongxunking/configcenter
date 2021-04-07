/*
 * 作者：钟勋 (email:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2021-04-07 22:51 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.PropertyChange;

/**
 * 合并分支-result
 */
@Getter
@Setter
public class MergeBranchResult extends AbstractResult {
    // 配置变动
    private PropertyChange propertyChange;
}
