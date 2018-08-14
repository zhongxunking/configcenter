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
     * 是否开启监听配置被修改事件的属性名（默认为开启）
     */
    public static final String LISTEN_ENABLE_PROPERTY_NAME = "configcenter.listen.enable";
    /**
     * 实例
     */
    public static final ConfigcenterProperties INSTANCE = Contexts.buildProperties(ConfigcenterProperties.class);

    /**
     * 必填：配置中心服务端地址（比如：http://192.168.0.1:6220）
     */
    @NotBlank
    private String serverUrl;
    /**
     * 选填：缓存目录（默认为：/var/apps/config/${appId}）
     */
    @NotBlank
    private String cacheDir = "/var/apps/config/" + Contexts.getAppId();
    /**
     * 选填：配置中心的配置优先于指定的配置源（默认为最低优先级）。可填入：commandLineArgs（命令行）、systemProperties（系统属性）、systemEnvironment（系统环境）、applicationConfigurationProperties（配置文件）等等
     */
    private String priorTo = null;
    /**
     * 选填：配置中心的配置是否比日志先初始化（默认为比日志后初始化）。
     * <p>
     * 比日志先初始化的好处：在配置中心的日志相关配置会生效；坏处：初始化配置中心的配置报错时，日志打印不出来。
     * 比日志后初始化的好处：初始化配置中心的配置报错时，能打印日志；坏处：在配置中心的日志相关配置不会生效（除了日志级别：logging.level.XXX）。
     * 总结：一般日志需要进行动态化的配置比较少（比如：日志格式、日志文件路径等），所以默认为比日志后初始化。
     */
    private boolean initBeforeLogging = false;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getCacheDir() {
        return cacheDir;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public String getPriorTo() {
        return priorTo;
    }

    public void setPriorTo(String priorTo) {
        this.priorTo = priorTo;
    }

    public boolean isInitBeforeLogging() {
        return initBeforeLogging;
    }

    public void setInitBeforeLogging(boolean initBeforeLogging) {
        this.initBeforeLogging = initBeforeLogging;
    }
}
