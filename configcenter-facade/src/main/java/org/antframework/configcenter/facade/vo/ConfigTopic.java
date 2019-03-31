/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-03-31 00:18 创建
 */
package org.antframework.configcenter.facade.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.tostring.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 配置主题
 */
@AllArgsConstructor
@Getter
public final class ConfigTopic implements Serializable {
    // 应用id
    private final String appId;
    // 环境id
    private final String profileId;

    @Override
    public int hashCode() {
        return Objects.hash(appId, profileId);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConfigTopic)) {
            return false;
        }
        ConfigTopic other = (ConfigTopic) obj;
        return Objects.equals(appId, other.appId) && Objects.equals(profileId, other.profileId);
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
