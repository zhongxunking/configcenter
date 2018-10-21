/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-20 21:57 创建
 */
package org.antframework.configcenter.facade.info;

import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.facade.vo.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用在环境中的配置
 */
public class ProfileProperty implements Serializable {
    // 环境id
    private String profileId;
    // 属性
    private List<Property> properties = new ArrayList<>();

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
