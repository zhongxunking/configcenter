/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 13:44 创建
 */
package org.antframework.configcenter.facade.api.manage;

import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.manage.TriggerClientRefreshOrder;

/**
 * 刷新服务
 */
public interface RefreshService {

    /**
     * 同步所有环境、应用到zookeeper
     */
    EmptyResult syncDataToZk(EmptyOrder order);

    /**
     * 触发客户端刷新配置
     */
    EmptyResult triggerClientRefresh(TriggerClientRefreshOrder order);
}
