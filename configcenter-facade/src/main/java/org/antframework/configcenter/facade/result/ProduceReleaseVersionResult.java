/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 17:18 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;

/**
 * 生产发布版本result
 */
@Getter
@Setter
public class ProduceReleaseVersionResult extends AbstractResult {
    // 发布版本
    private long releaseVersion;
}
