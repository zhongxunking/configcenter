/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 18:56 创建
 */
package org.antframework.configcenter.web.manager.facade.result;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.web.manager.facade.info.ManagerAppInfo;

/**
 * 查找管理员与应用关联result
 */
public class FindManagerAppResult extends AbstractResult {
    // 管理员与应用关联信息
    private ManagerAppInfo info;

    public ManagerAppInfo getInfo() {
        return info;
    }

    public void setInfo(ManagerAppInfo info) {
        this.info = info;
    }
}
