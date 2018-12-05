/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 21:14 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

/**
 * 查找环境树order
 */
@Getter
@Setter
public class FindProfileTreeOrder extends AbstractOrder {
    // 根节点环境id（null表示查找所有环境）
    private String profileId;
}
