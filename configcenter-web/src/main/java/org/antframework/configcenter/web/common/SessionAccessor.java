/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 22:20 创建
 */
package org.antframework.configcenter.web.common;

import org.antframework.configcenter.web.manager.dal.entity.Manager;

import javax.servlet.http.HttpSession;

/**
 *
 */
public class SessionAccessor {
    private static final ThreadLocal<HttpSession> SESSION_HOLDER = new ThreadLocal<>();
    private static final String KEY_MANAGER = "KEY_MANAGER";

    public static void setSession(HttpSession session) {
        SESSION_HOLDER.set(session);
    }

    public static void setManager(Manager manager) {
        SESSION_HOLDER.get().setAttribute(KEY_MANAGER, manager);
    }

    public static void removeManager() {
        SESSION_HOLDER.get().removeAttribute(KEY_MANAGER);
    }

    public static Manager getManager() {
        return (Manager) SESSION_HOLDER.get().getAttribute(KEY_MANAGER);
    }
}
