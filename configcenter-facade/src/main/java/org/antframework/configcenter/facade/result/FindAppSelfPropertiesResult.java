/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:26 创建
 */
package org.antframework.configcenter.facade.result;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ProfileProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用自己的在特定环境中的配置result
 */
public class FindAppSelfPropertiesResult extends AbstractResult {
    // 由近及远继承的所用环境中的配置
    private List<ProfileProperty> profileProperties = new ArrayList<>();

    public List<ProfileProperty> getProfileProperties() {
        return profileProperties;
    }

    public void addProfileProperty(ProfileProperty profileProperty) {
        profileProperties.add(profileProperty);
    }
}
