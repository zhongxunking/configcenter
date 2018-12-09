/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-09 17:34 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ReleaseInfo;

/**
 * 查找当前发布result
 */
@Getter
@Setter
public class FindCurrentReleaseResult extends AbstractResult {
    // 发布（null表示无任何发布）
    private ReleaseInfo release;
}
