/* 
 * Copyright © 2017 www.lvmama.com
 */

/*
 * 修订记录:
 * @author 钟勋（zhongxun@lvmama.com） 2017-08-23 21:22 创建
 */
package org.antframework.configcenter.web.manager.facade.enums;

/**
 *
 */
public enum ResultCode {

    ILLEGAL_STATE("configcenter-0001", "非法内部状态"),

    ;

    // 结果码
    private String code;

    // 描述
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取结果码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取描述
     */
    public String getMessage() {
        return message;
    }
}
