/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 20:57 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

/**
 * 新增发布order
 */
@Getter
@Setter
public class AddReleaseOrder extends AbstractOrder {
    // 应用id
    private String appId;
    // 环境id
    private String profileId;
    // 备注
    private String memo;
}
