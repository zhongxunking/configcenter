/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:47 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;

/**
 * 应用info
 */
@Getter
@Setter
public class AppInfo extends AbstractInfo {
    // 应用id
    private String appId;
    // 应用名
    private String appName;
    // 发布版本
    private Long releaseVersion;
    // 父应用id（null表示无父应用）
    private String parent;
}
