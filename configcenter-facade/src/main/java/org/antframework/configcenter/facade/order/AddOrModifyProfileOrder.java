/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:20 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

import javax.validation.constraints.NotBlank;

/**
 * 添加或修改环境order
 */
@Getter
@Setter
public class AddOrModifyProfileOrder extends AbstractOrder {
    // 环境id
    @NotBlank
    private String profileId;
    // 环境名
    private String profileName;
    // 父环境id（null表示无父环境）
    private String parent;
}
