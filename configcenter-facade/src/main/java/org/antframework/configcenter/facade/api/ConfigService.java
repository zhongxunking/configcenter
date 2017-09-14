/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:09 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.order.QueryPropertiesOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.antframework.configcenter.facade.result.QueryPropertiesResult;

/**
 * 配置服务
 */
public interface ConfigService {
    /**
     * 公共配置应用编码
     */
    String COMMON_APP_CODE = "common";

    /**
     * 查找应用
     */
    FindAppResult findApp(FindAppOrder order);

    /**
     * 查询应用在特定环境中的配置
     */
    QueryPropertiesResult queryProperties(QueryPropertiesOrder order);
}
