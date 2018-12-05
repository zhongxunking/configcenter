/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-02 13:03 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.Scope;

/**
 * 配置key-info
 */
@Getter
@Setter
public class PropertyKeyInfo extends AbstractInfo {
    // 应用id
    private String appId;
    // key
    private String key;
    // 作用域
    private Scope scope;
    // 备注
    private String memo;
}
