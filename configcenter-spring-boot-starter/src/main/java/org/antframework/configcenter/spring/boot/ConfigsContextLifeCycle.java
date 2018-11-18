/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-03 10:25 创建
 */
package org.antframework.configcenter.spring.boot;

import org.antframework.configcenter.client.Config;
import org.antframework.configcenter.client.ConfigsContext;
import org.antframework.configcenter.spring.ConfigsContexts;
import org.antframework.configcenter.spring.context.Contexts;
import org.antframework.configcenter.spring.listener.DefaultConfigListener;
import org.antframework.configcenter.spring.listener.annotation.ConfigListenerType;
import org.bekit.event.EventPublisher;
import org.bekit.event.bus.EventBusesHolder;
import org.bekit.event.publisher.DefaultEventPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 配置上下文的生命周期
 */
public class ConfigsContextLifeCycle implements GenericApplicationListener {
    // 刷新定时器
    private Timer refreshTimer;

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
            readyConfigsContext();
            initTimer();
        } else {
            close();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    // 使配置上下文准备好
    private void readyConfigsContext() {
        ConfigsContext configsContext = ConfigsContexts.getContext();
        // 创建事件发布器
        EventBusesHolder eventBusesHolder = Contexts.getApplicationContext().getBean(EventBusesHolder.class);
        EventPublisher eventPublisher = new DefaultEventPublisher(eventBusesHolder.getEventBus(ConfigListenerType.class));
        // 添加默认监听器
        for (String appId : configsContext.getAppIds()) {
            Config config = configsContext.getConfig(appId);
            config.getListenerRegistrar().register(new DefaultConfigListener(appId, eventPublisher));
        }
        // 判断是否监听配置被修改事件
        boolean enable = Contexts.getEnvironment().getProperty(ConfigcenterProperties.LISTEN_ENABLE_PROPERTY_NAME, Boolean.class, Boolean.TRUE);
        if (enable) {
            // 开始监听配置是否被修改
            configsContext.listenConfigChanged();
        }
    }

    // 初始化刷新定时器
    private void initTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ConfigsContexts.getContext().refresh();
            }
        };

        refreshTimer = new Timer("Timer-refreshConfigsContext", true);
        // 应用启动期间配置有可能被修改，在此立即触发一次刷新
        refreshTimer.schedule(task, 0, ConfigcenterProperties.INSTANCE.getRefreshPeriod() * 1000);
    }

    // 关闭配置上下文和刷新定时器
    private void close() {
        ConfigsContexts.getContext().close();
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
    }
}
