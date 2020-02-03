/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-03-31 17:41 创建
 */
package org.antframework.configcenter.web.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.vo.ConfigTopic;
import org.antframework.configcenter.facade.vo.RefreshClientsEvent;
import org.bekit.event.annotation.DomainListener;
import org.bekit.event.annotation.Listen;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听刷新事件的客户端的容器
 */
@DomainListener
public class ListeningClientsContainer {
    // 配置主题及对应的客户端
    private final Map<ConfigTopic, Set<ListeningClient>> topicClients = new ConcurrentHashMap<>();

    /**
     * 添加客户端
     *
     * @param client 客户端
     */
    public void addClient(ListeningClient client) {
        for (ConfigTopic topic : client.getTopics()) {
            topicClients.compute(topic, (key, value) -> {
                if (value == null) {
                    value = new HashSet<>();
                }
                value.add(client);
                return value;
            });
        }
    }

    /**
     * 删除客户端
     *
     * @param client 客户端
     */
    public void removeClient(ListeningClient client) {
        for (ConfigTopic topic : client.getTopics()) {
            topicClients.compute(topic, (key, value) -> {
                if (value == null) {
                    return null;
                }
                value.remove(client);
                return value.isEmpty() ? null : value;
            });
        }
    }

    /**
     * 监听刷新客户端事件
     *
     * @param event 刷新客户端事件
     */
    @Listen
    public void onRefreshClients(RefreshClientsEvent event) {
        event.getTopics().stream()
                .map(topicClients::get)
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .distinct()
                .forEach(client -> {
                    ListenResult listenResult = FacadeUtils.buildSuccess(ListenResult.class);
                    for (ConfigTopic topic : event.getTopics()) {
                        if (client.getTopics().contains(topic)) {
                            listenResult.addTopic(topic);
                        }
                    }
                    client.getDeferredResult().setResult(listenResult);
                });
    }

    /**
     * 监听刷新事件的客户端
     */
    @AllArgsConstructor
    @Getter
    public static class ListeningClient {
        // 监听的配置主题
        private Set<ConfigTopic> topics;
        // http异步返回结果
        private DeferredResult<ListenResult> deferredResult;
    }

    /**
     * 监听结果
     */
    @Getter
    public static class ListenResult extends AbstractResult {
        // 需客户端刷新的配置主题
        private final Set<ConfigTopic> topics = new HashSet<>();

        public void addTopic(ConfigTopic topic) {
            topics.add(topic);
        }
    }
}
