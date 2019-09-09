/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-21 22:15 创建
 */
package org.antframework.configcenter.facade.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.facade.AbstractInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用树
 */
@AllArgsConstructor
@Getter
public class AppTree extends AbstractInfo {
    // 根节点
    private final AppInfo app;
    // 子树
    private final List<AppTree> children = new ArrayList<>();

    public void addChild(AppTree child) {
        children.add(child);
    }
}
