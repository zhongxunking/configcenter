/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 13:43 创建
 */
package org.antframework.configcenter.web.manager.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.common.util.tostring.format.Mask;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 管理员登陆order
 */
public class ManagerLoginOrder extends AbstractOrder {
    // 管理员编码
    @NotBlank
    private String managerCode;
    // 密码
    @NotBlank
    @Mask(allMask = true)
    private String password;

    public String getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(String managerCode) {
        this.managerCode = managerCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
