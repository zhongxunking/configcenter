/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 15:59 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

/**
 * 刷新客户端order
 */
@Getter
@Setter
public class RefreshClientsOrder extends AbstractOrder {
    // 根应用id（null表示刷新所有应用）
    private String rootAppId;
    // 根环境id（null表示刷新所有环境）
    private String rootProfileId;
}
