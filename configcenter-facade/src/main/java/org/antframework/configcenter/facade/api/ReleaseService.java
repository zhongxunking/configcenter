/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 20:55 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.AddReleaseOrder;
import org.antframework.configcenter.facade.order.RevertReleaseOrder;
import org.antframework.configcenter.facade.result.AddReleaseResult;

/**
 * 发布服务
 */
public interface ReleaseService {
    /**
     * 新增发布
     */
    AddReleaseResult addRelease(AddReleaseOrder order);

    /**
     * 回滚发布
     */
    EmptyResult revertRelease(RevertReleaseOrder order);
}
