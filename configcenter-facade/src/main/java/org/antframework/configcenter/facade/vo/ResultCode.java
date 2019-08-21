/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-19 14:42 创建
 */
package org.antframework.configcenter.facade.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结果码
 */
@AllArgsConstructor
@Getter
public enum ResultCode {

    EXISTS_CHILDREN("configcenter-0000", "存在子节点");

    // 结果码
    private final String code;
    // 描述
    private final String message;
}
