/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 10:59 创建
 */
package org.antframework.configcenter.web.manager.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.common.util.tostring.format.Mask;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 修改密码order
 */
public class ModifyManagerPasswordOrder extends AbstractOrder {
    // 管理员编码
    @NotBlank
    private String managerCode;
    // 新密码
    @NotBlank
    @Mask(allMask = true)
    private String newPassword;

    public String getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(String managerCode) {
        this.managerCode = managerCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
