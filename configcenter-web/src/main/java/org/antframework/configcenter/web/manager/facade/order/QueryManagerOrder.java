/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:14 创建
 */
package org.antframework.configcenter.web.manager.facade.order;

import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;

/**
 * 查询管理员order
 */
public class QueryManagerOrder extends AbstractQueryOrder {
    // 用户名
    private String username;
    // 类型
    private ManagerType type;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ManagerType getType() {
        return type;
    }

    public void setType(ManagerType type) {
        this.type = type;
    }
}
