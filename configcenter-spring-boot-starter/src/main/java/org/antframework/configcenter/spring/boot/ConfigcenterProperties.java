/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-14 01:02 创建
 */
package org.antframework.configcenter.spring.boot;

import org.antframework.configcenter.spring.context.Contexts;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 配置中心属性
 */
@ConfigurationProperties("configcenter")
@Validated
public class ConfigcenterProperties {
    /**
     * 应用id的占位符匹配模式
     */
    public static final String APP_ID_PATTERN = "${configcenter.app-id:${spring.application.name}}";
    /**
     * 是否开启监听的属性名
     */
    public static final String LISTEN_ENABLE_PROPERTY_NAME = "configcenter.listen.enable";

    /**
     * 必填：配置中心服务端地址（比如：http://192.168.0.1:6220）
     */
    @NotBlank
    private String serverUrl;
    /**
     * 选填：缓存文件目录路径（默认为：/var/apps/config/${appId}）
     */
    @NotBlank
    private String cacheDirPath = "/var/apps/config/" + Contexts.getAppId();

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getCacheDirPath() {
        return cacheDirPath;
    }

    public void setCacheDirPath(String cacheDirPath) {
        this.cacheDirPath = cacheDirPath;
    }
}