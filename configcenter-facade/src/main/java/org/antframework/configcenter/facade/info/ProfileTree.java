/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 21:08 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.facade.AbstractInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 环境树
 */
@AllArgsConstructor
@Getter
public class ProfileTree extends AbstractInfo {
    // 根节点
    private final ProfileInfo profile;
    // 子树
    private final List<ProfileTree> children = new ArrayList<>();

    public void addChild(ProfileTree child) {
        children.add(child);
    }
}
