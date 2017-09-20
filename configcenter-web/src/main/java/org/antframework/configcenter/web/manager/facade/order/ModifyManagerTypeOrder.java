/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:06 创建
 */
package org.antframework.configcenter.web.manager.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 修改管理员类型order
 */
public class ModifyManagerTypeOrder extends AbstractOrder {
    // 编码
    @NotBlank
    private String code;
    // 新类型
    @NotNull
    private ManagerType newType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ManagerType getNewType() {
        return newType;
    }

    public void setNewType(ManagerType newType) {
        this.newType = newType;
    }
}
