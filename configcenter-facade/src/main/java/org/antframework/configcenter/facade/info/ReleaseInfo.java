/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 20:59 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.Property;

import java.util.Date;
import java.util.Set;

/**
 * 发布info
 */
@Getter
@Setter
public class ReleaseInfo extends AbstractInfo {
    // 应用id
    private String appId;
    // 环境id
    private String profileId;
    // 版本
    private Long version;
    // 发布时间
    private Date releaseTime;
    // 备注
    private String memo;
    // 配置集
    private Set<Property> properties;
}
