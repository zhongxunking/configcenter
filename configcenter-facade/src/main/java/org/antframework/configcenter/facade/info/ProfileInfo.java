/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:33 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;

/**
 * 环境info
 */
@Getter
@Setter
public class ProfileInfo extends AbstractInfo {
    // 环境id
    private String profileId;
    // 环境名
    private String profileName;
    // 父环境id（null表示无父环境）
    private String parent;
}
