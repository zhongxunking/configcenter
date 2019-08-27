/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-27 21:27 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ReleaseInfo;

/**
 * 删除发布result
 */
@Getter
@Setter
public class DeleteReleaseResult extends AbstractResult {
    // 被删除的发布（null表示不存在该发布）
    private ReleaseInfo release;
}
