/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:42 创建
 */
package org.antframework.configcenter.biz;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antframework.configcenter.facade.vo.RedisConstant;
import org.antframework.configcenter.facade.vo.RefreshClientsEvent;
import org.bekit.event.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * biz层配置
 */
@Configuration
@AllArgsConstructor
@Slf4j
public class BizConfiguration {
    // redis操作类
    private final RedisTemplate<Object, Object> redisTemplate;
    // 事件发布器
    private final EventPublisher eventPublisher;

    // 配置redis消息监听器容器
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(new RefreshClientsMessageListener(), new ChannelTopic(RedisConstant.REFRESH_CLIENTS_CHANNEL));
        return container;
    }

    // 刷新客户端消息监听器
    private class RefreshClientsMessageListener implements MessageListener {
        @Override
        public void onMessage(Message message, byte[] pattern) {
            RefreshClientsEvent event = (RefreshClientsEvent) redisTemplate.getValueSerializer().deserialize(message.getBody());
            log.debug("从Redis接收到刷新客户端消息：{}", event);
            eventPublisher.publish(event);
        }
    }
}
