/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:04 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.tostring.hidedetail.HideDetail;

import java.util.Map;

/**
 * 查找应用在指定环境中的配置result
 */
@Getter
@Setter
public class FindConfigResult extends AbstractResult {
    // 版本
    private Long version;
    // 配置
    @HideDetail
    private Map<String, String> properties;
}
