/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-21 22:15 创建
 */
package org.antframework.configcenter.facade.info;

import org.antframework.common.util.tostring.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用树
 */
public class AppTree implements Serializable {
    // 根节点
    private AppInfo app;
    // 子树
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

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
