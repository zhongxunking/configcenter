/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-06-22 11:34 创建
 */
package org.antframework.template.assemble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动入口
 */
@SpringBootApplication
public class Main {
    private static final String PROFILE_OS_PROPERTY_KEY = "SPRING_PROFILES_ACTIVE";
    private static final String PROFILE_SYS_PROPERTY_KEY = "spring.profiles.active";

    public static void main(String[] args) {
        setProfileIfNotExists("dev");
        SpringApplication.run(Main.class, args);
    }

    // 设置运行环境（如果未设置）
    private static void setProfileIfNotExists(String profile) {
        if (System.getenv(PROFILE_OS_PROPERTY_KEY) == null && System.getProperty(PROFILE_SYS_PROPERTY_KEY) == null) {
            System.setProperty(PROFILE_SYS_PROPERTY_KEY, profile);
        }
    }
}
