/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-11-11 23:26 创建
 */
package org.antframework.configcenter.web.common;

/**
 * 安全等级
 */
public enum SecurityLevel {
    /**
     * 能读也能写
     */
    READABLE_WRITEABLE,
    /**
     * 能读但不能写
     */
    READABLE_UNWRITEABLE,
    /**
     * 不能读也不能写
     */
    UNREADABLE_UNWRITEABLE
}
