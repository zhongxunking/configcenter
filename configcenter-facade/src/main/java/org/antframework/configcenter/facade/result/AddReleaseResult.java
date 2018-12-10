/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 20:59 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ReleaseInfo;

/**
 * 新增发布result
 */
@Getter
@Setter
public class AddReleaseResult extends AbstractResult {
    // 新增的发布
    private ReleaseInfo release;
}
