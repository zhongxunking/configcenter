/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 16:03 创建
 */
package org.antframework.configcenter.facade.vo;

/**
 * 作用域（依次从小到大）
 */
public enum Scope {
    /**
     * 私有
     */
    PRIVATE,

    /**
     * 可继承
     */
    PROTECTED,

    /**
     * 公开
     */
    PUBLIC
}
