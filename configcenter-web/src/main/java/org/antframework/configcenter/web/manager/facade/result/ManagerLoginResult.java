/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 13:44 创建
 */
package org.antframework.configcenter.web.manager.facade.result;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.web.manager.facade.info.ManagerInfo;

/**
 * 管理员登陆result
 */
public class ManagerLoginResult extends AbstractResult {
    // 管理员info
    private ManagerInfo managerInfo;

    public ManagerInfo getManagerInfo() {
        return managerInfo;
    }

    public void setManagerInfo(ManagerInfo managerInfo) {
        this.managerInfo = managerInfo;
    }
}
