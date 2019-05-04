/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-14 01:02 创建
 */
package org.antframework.configcenter.spring.boot;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.tostring.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

/**
 * configcenter属性
 */
@ConfigurationProperties("configcenter")
@Validated
public class ConfigcenterProperties {
    /**
     * 是否开启自动刷新configcenter配置（默认为开启）
     */
    public static final String AUTO_REFRESH_CONFIGS_ENABLE_KEY = "configcenter.auto-refresh-configs.enable";
    /**
     * 自动刷新configcenter配置的周期（单位：毫秒。默认为5分钟刷新一次）
     */
    public static final String AUTO_REFRESH_CONFIGS_PERIOD_KEY = "configcenter.auto-refresh-configs.period";
    /**
     * 实例
     */
    public static final ConfigcenterProperties INSTANCE = Contexts.buildProperties(ConfigcenterProperties.class);

    /**
     * 选填：应用id（如果未设置，则采用spring.application.name对应的值）
     */
    private String appId = null;
    /**
     * 选填：环境id（如果未设置，则采用spring.profiles.active对应的值）
     */
    private String profileId = null;
    /**
     * 必填：configcenter服务端地址（比如：http://192.168.0.1:6220）
     */
    @NotBlank
    private String serverUrl;
    /**
     * 选填：缓存目录（默认为：/var/apps/${appId}/configcenter）
     */
    @NotBlank
    private String home = Contexts.getHome() + "/configcenter";
    /**
     * 选填：configcenter配置优先于指定的配置源（默认为最低优先级）。可填入：commandLineArgs（命令行）、systemProperties（系统属性）、systemEnvironment（系统环境）、random（随机数。比配置文件优先级高）等等
     */
    private String priorTo = null;

    public String getRequiredAppId() {
        String appId = this.appId;
        if (appId == null) {
            appId = Contexts.getAppId();
        }
        Assert.notNull(appId, String.format("未设置应用id（配置key：configcenter.app-id或者%s）", Contexts.APP_ID_KEY));
        return appId;
    }

    public String getRequiredProfileId() {
        if (profileId != null) {
            return profileId;
        }
        String[] profileIds = Contexts.getEnvironment().getActiveProfiles();
        if (profileIds.length <= 0) {
            throw new IllegalArgumentException(String.format("未设置环境id（配置key：configcenter.profile-id或者%s）", AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME));
        } else if (profileIds.length > 1) {
            throw new IllegalArgumentException(String.format("只能设置一个环境id（配置key：configcenter.profile-id或者%s），当前设置了多个环境id%s", AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, ToString.toString(profileIds)));
        }
        return profileIds[0];
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getPriorTo() {
        return priorTo;
    }

    public void setPriorTo(String priorTo) {
        this.priorTo = priorTo;
    }
}
