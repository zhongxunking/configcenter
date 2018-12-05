/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:53 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.Scope;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 添加或修改配置key-order
 */
@Getter
@Setter
public class AddOrModifyPropertyKeyOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
    // key
    @NotBlank
    private String key;
    // 作用域
    @NotNull
    private Scope scope;
    // 备注
    private String memo;
}
