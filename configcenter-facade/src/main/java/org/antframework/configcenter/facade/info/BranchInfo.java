/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:42 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;

/**
 * 分支info
 */
@Getter
@Setter
public class BranchInfo extends AbstractInfo {
    // 应用id
    private String appId;
    // 环境id
    private String profileId;
    // 分支id
    private String branchId;
    // 发布
    private ReleaseInfo release;
}
