/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-06-22 11:34 创建
 */
package org.antframework.configcenter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

/**
 * 程序启动入口
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        setProfileIfNotExists("offline");
        SpringApplication.run(Main.class, args);
    }

    // 设置运行环境（如果未设置）
    private static void setProfileIfNotExists(String profile) {
        String activeProfilesEnvPropertyName = AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME.toUpperCase().replace('.', '_');
        if (StringUtils.isEmpty(System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME))
                && StringUtils.isEmpty(System.getenv(activeProfilesEnvPropertyName))) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
        }
    }
}
