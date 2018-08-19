/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-03 10:25 创建
 */
package org.antframework.configcenter.spring.boot;

import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.client.support.Config;
import org.antframework.configcenter.spring.ConfigContexts;
import org.antframework.configcenter.spring.context.Contexts;
import org.antframework.configcenter.spring.listener.DefaultConfigListener;
import org.antframework.configcenter.spring.listener.annotation.ConfigListenerType;
import org.bekit.event.EventPublisher;
import org.bekit.event.bus.EventBusesHolder;
import org.bekit.event.publisher.DefaultEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

/**
 * 配置上下文的生命周期
 */
public class ConfigContextLifeCycle implements GenericApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(ConfigContextLifeCycle.class);

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        return ApplicationReadyEvent.class.isAssignableFrom(eventType.getRawClass())
                || ApplicationFailedEvent.class.isAssignableFrom(eventType.getRawClass())
                || ContextClosedEvent.class.isAssignableFrom(eventType.getRawClass());
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return SpringApplication.class.isAssignableFrom(sourceType)
                || ApplicationContext.class.isAssignableFrom(sourceType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            readyConfigContexts();
        } else {
            closeConfigContexts();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    // 使配置上下文准备好
    private void readyConfigContexts() {
        ConfigContext configContext = ConfigContexts.getContext();
        // 创建事件发布器
        EventBusesHolder eventBusesHolder = Contexts.getApplicationContext().getBean(EventBusesHolder.class);
        EventPublisher eventPublisher = new DefaultEventPublisher(eventBusesHolder.getEventBus(ConfigListenerType.class));
        // 添加默认监听器
        for (String appId : configContext.getAppIds()) {
            Config config = configContext.getConfig(appId);
            config.getListenerRegistrar().register(new DefaultConfigListener(appId, eventPublisher));
        }
        // 判断是否监听配置被修改
        boolean listenEnable = Contexts.getEnvironment().getProperty(ConfigcenterProperties.LISTEN_ENABLE_PROPERTY_NAME, Boolean.class, Boolean.TRUE);
        if (listenEnable) {
            // 开始监听配置是否被修改
            configContext.listenConfigChanged();
            // 刷新配置（应用启动期间配置有可能被修改，在此触发一次刷新）
            configContext.refresh();
        }
    }

    // 关闭配置上下文
    private void closeConfigContexts() {
        ConfigContexts.getContext().close();
    }
}
