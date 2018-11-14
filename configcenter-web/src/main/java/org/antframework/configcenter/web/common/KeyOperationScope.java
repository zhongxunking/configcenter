/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-11-14 21:54 创建
 */
package org.antframework.configcenter.web.common;

/**
 * 配置key的可操作范围
 */
public enum KeyOperationScope {
    /**
     * 读写
     */
    READ_WRITE,
    /**
     * 只读
     */
    READ,
    /**
     * 无
     */
    NONE
}
