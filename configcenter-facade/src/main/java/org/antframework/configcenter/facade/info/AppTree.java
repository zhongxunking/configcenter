/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-21 22:15 创建
 */
package org.antframework.configcenter.facade.info;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用树
 */
public class AppTree {
    // 根节点
    private AppInfo app;
    // 子节点
    private List<AppTree> children = new ArrayList<>();

    public AppTree(AppInfo app) {
        this.app = app;
    }

    public AppInfo getApp() {
        return app;
    }

    public List<AppTree> getChildren() {
        return children;
    }

    public void addChild(AppTree child) {
        children.add(child);
    }
}
