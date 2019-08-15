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
import org.antframework.configcenter.facade.vo.Scope;

/**
 * 配置value-info
 */
@Getter
@Setter
public class PropertyValueInfo extends AbstractInfo {
    // 应用id
    private String appId;
    // 环境id
    private String profileId;
    // key
    private String key;
    // value
    @Mask(allMask = true)
    private String value;
    // 作用域
    private Scope scope;
}
