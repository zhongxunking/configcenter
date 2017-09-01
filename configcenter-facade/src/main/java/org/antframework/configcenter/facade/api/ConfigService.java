/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:09 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.order.QueryAppProfilePropertyOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.QueryAppProfilePropertyResult;

/**
 *
 */
public interface ConfigService {

    String PROFILE_COMMON_APP_CODE = "common";

    FindAppResult findApp(FindAppOrder order);

    QueryAppProfilePropertyResult queryAppProfileProperty(QueryAppProfilePropertyOrder order);
}
