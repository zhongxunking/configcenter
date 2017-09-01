/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:48 创建
 */
package org.antframework.configcenter;

import org.antframework.boot.core.AntBootApplication;
import org.antframework.boot.core.Apps;
import org.springframework.boot.SpringApplication;

/**
 * 程序启动入口
 */
@AntBootApplication(appCode = "configcenter", httpPort = 8080)
public class Main {
    public static void main(String[] args) {
        Apps.setProfileIfNotExists("offline");
        SpringApplication.run(Main.class, args);
    }
}
