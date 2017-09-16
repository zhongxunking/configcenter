/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 13:44 创建
 */
package org.antframework.configcenter.facade.api.manage;

import org.antframework.configcenter.facade.order.manage.SyncDataToZkOrder;
import org.antframework.configcenter.facade.result.manage.SyncDataToZkResult;

/**
 * 刷新服务
 */
public interface RefreshService {

    /**
     * 同步同步所有环境、应用到zookeeper
     */
    SyncDataToZkResult syncDataToZk(SyncDataToZkOrder order);

}
