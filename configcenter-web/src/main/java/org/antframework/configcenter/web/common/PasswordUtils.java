/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-22 21:13 创建
 */
package org.antframework.configcenter.web.common;

import org.springframework.util.DigestUtils;

/**
 * 密码工具类
 */
public class PasswordUtils {

    /**
     * 加密
     *
     * @param password 密码
     */
    public static String digest(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
