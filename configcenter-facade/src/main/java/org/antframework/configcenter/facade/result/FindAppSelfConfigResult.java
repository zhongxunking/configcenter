/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:26 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ReleaseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用自己的在指定环境中的配置result
 */
@Getter
public class FindAppSelfConfigResult extends AbstractResult {
    // 由近及远继承的所用环境中的配置
    private final List<ReleaseInfo> inheritedReleases = new ArrayList<>();

    public void addInheritedRelease(ReleaseInfo release) {
        inheritedReleases.add(release);
    }
}
