/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 17:16 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

/**
 * 生产配置发布版本order
 */
@Getter
@Setter
public class ProduceReleaseVersionOrder extends AbstractOrder {
    // 应用id
    private String appId;
}
