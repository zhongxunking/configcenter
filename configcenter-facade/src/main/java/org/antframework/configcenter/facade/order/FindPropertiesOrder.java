/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:03 创建
 */
package org.antframework.configcenter.facade.order;

import org.antframework.common.util.facade.AbstractOrder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 查找应用在指定环境中的配置order
 */
public class FindPropertiesOrder extends AbstractOrder {
    // 主体应用id
    @NotBlank
    private String mainAppId;
    // 被查询配置的应用id
    @NotBlank
    private String queriedAppId;
    // 环境id
    @NotBlank
    private String profileId;

    public String getMainAppId() {
        return mainAppId;
    }

    public void setMainAppId(String mainAppId) {
        this.mainAppId = mainAppId;
    }

    public String getQueriedAppId() {
        return queriedAppId;
    }

    public void setQueriedAppId(String queriedAppId) {
        this.queriedAppId = queriedAppId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
