/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-14 01:02 创建
 */
package org.antframework.configcenter.spring.boot;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.kit.IPUtils;
import org.antframework.common.util.tostring.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * configcenter属性
 */
@ConfigurationProperties("configcenter")
@Validated
public class ConfigcenterProperties {
    /**
     * configcenter初始化的优先级的key（默认为Ordered.HIGHEST_PRECEDENCE + 30，比日志初始化的优先级低）
     */
    public static final String INIT_ORDER_KEY = "configcenter.init-order";
    /**
     * 是否开启自动刷新configcenter配置的key（默认为开启）
     */
    public static final String AUTO_REFRESH_CONFIGS_ENABLE_KEY = "configcenter.auto-refresh-configs.enable";
    /**
     * 自动刷新configcenter配置的周期的key（单位：毫秒。默认为5分钟刷新一次）
     */
    public static final String AUTO_REFRESH_CONFIGS_PERIOD_KEY = "configcenter.auto-refresh-configs.period";
    /**
     * 实例
     */
    public static final ConfigcenterProperties INSTANCE = Contexts.buildProperties(ConfigcenterProperties.class);

    /**
     * 选填：是否启用configcenter（默认启用）
     */
    private boolean enable = true;
    /**
     * 选填：应用id（默认为spring.application.name对应的值）
     */
    private String appId = null;
    /**
     * 选填：环境id（默认为spring.profiles.active对应的值）
     */
    private String profileId = null;
    /**
     * 选填：目标（用于标记客户端，默认为客户端所在主机的IP地址）
     */
    private String target = IPUtils.getIPV4();
    /**
     * 必填：configcenter服务端地址（比如：http://192.168.0.1:6220）
     */
    @NotBlank
    private String serverUrl;
    /**
     * 选填：缓存目录（"false"表示关闭缓存功能，默认为：/var/apps/${appId}/configcenter）
     */
    @NotBlank
    private String home = Contexts.getHome() + "/configcenter";
    /**
     * 选填：管理员id（默认不使用管理员签名）
     */
    private String managerId = null;
    /**
     * 选填：管理员密钥（默认不使用管理员签名）
     */
    private String secretKey = null;
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

    public String computeHome() {
        if (Objects.equals(home, Boolean.FALSE.toString())) {
            return null;
        }
        return home;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPriorTo() {
        return priorTo;
    }

    public void setPriorTo(String priorTo) {
        this.priorTo = priorTo;
    }
}
