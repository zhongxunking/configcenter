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
    // 编码
    private String code;
    // 名称
    private String name;
    // 类型
    private ManagerType type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ManagerType getType() {
        return type;
    }

    public void setType(ManagerType type) {
        this.type = type;
    }
}
