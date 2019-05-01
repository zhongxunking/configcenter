/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:11 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

import javax.validation.constraints.NotBlank;

/**
 * 添加或修改应用order
 */
@Getter
@Setter
public class AddOrModifyAppOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
    // 应用名
    private String appName;
    // 父应用id（null表示无父应用）
    private String parent;
}
