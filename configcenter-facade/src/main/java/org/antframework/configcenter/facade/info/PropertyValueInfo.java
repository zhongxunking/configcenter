/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-02 19:37 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.common.util.tostring.format.Mask;

/**
 * 配置value-info
 */
@Getter
@Setter
public class PropertyValueInfo extends AbstractInfo {
    // 应用id
    private String appId;
    // key
    private String key;
    // 环境id
    private String profileId;
    // value
    @Mask(allMask = true)
    private String value;
}
