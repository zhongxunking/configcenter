/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 12:40 创建
 */
package org.antframework.configcenter.common;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * zookeeper工具
 */
public class ZkUtils {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 获取当前时间
     */
    public static byte[] getCurrentDate() {
        return DateFormatUtils.format(new Date(), DATE_PATTERN).getBytes(Charset.forName("utf-8"));
    }
}
