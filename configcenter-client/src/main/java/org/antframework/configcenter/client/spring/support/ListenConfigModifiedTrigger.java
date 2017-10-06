/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 22:21 创建
 */
package org.antframework.configcenter.client.spring.support;

import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.client.ConfigListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 监听配置是否被修改触发器
 */
public class ListenConfigModifiedTrigger implements ApplicationListener<ContextRefreshedEvent> {
    // 配置上下文
    private ConfigContext configContext;
    // 配置监听器
    private ConfigListener[] configListeners;

    public ListenConfigModifiedTrigger(ConfigContext configContext, ConfigListener... configListeners) {
        this.configContext = configContext;
        this.configListeners = configListeners;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 注册配置监听器
        for (ConfigListener listener : configListeners) {
            configContext.getListenerRegistrar().register(listener);
        }
        // 开始监听配置
        configContext.listenConfigModified();
        // 刷新配置（应用启动期间配置有可能被修改，在此触发一次刷新）
        configContext.refreshConfig();
    }
}
