/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 22:34 创建
 */
package org.antframework.configcenter.web.manager.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 修改管理员名称order
 */
public class ModifyManagerNameOrder extends AbstractOrder {
    // 编码
    @NotBlank
    private String code;
    // 新名称
    @NotBlank
    private String newName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
