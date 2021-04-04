/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:09 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.configcenter.facade.order.FindConfigOrder;
import org.antframework.configcenter.facade.order.FindInheritedAppReleasesOrder;
import org.antframework.configcenter.facade.result.FindConfigResult;
import org.antframework.configcenter.facade.result.FindInheritedAppReleasesResult;

/**
 * 配置服务
 */
public interface ConfigService {
    /**
     * 查找应用在指定环境中的配置
     */
    FindConfigResult findConfig(FindConfigOrder order);

    /**
     * 查找继承的应用发布
     */
    FindInheritedAppReleasesResult findInheritedAppReleases(FindInheritedAppReleasesOrder order);
}
