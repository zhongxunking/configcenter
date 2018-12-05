/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-22 20:59 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

/**
 * 查找应用树order
 */
@Getter
@Setter
public class FindAppTreeOrder extends AbstractOrder {
    // 根节点应用id（null表示查找所有应用）
    private String appId;
}
