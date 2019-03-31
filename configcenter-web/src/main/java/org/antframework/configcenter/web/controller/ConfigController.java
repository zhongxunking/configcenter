/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 16:11 创建
 */
package org.antframework.configcenter.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindPropertiesResult;
import org.antframework.configcenter.facade.vo.ConfigTopic;
import org.antframework.configcenter.web.common.ListeningClientsContainer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 配置controller
 */
@RestController
@RequestMapping("/config")
@AllArgsConstructor
public class ConfigController {
    // 配置服务
    private final ConfigService configService;
    // 监听刷新事件的客户端的容器
    private final ListeningClientsContainer listeningClientsContainer;

    /**
     * 查找应用在指定环境中的配置
     *
     * @param mainAppId    主体应用id（必须）
     * @param queriedAppId 被查询配置的应用id（必须）
     * @param profileId    环境id（必须）
     */
    @RequestMapping("/findProperties")
    public FindPropertiesResult findProperties(String mainAppId, String queriedAppId, String profileId) {
        FindPropertiesOrder order = new FindPropertiesOrder();
        order.setMainAppId(mainAppId);
        order.setQueriedAppId(queriedAppId);
        order.setProfileId(profileId);

        return configService.findProperties(order);
    }

    /**
     * 监听刷新客户端事件
     *
     * @param listenMetas 监听元数据（必须）
     */
    @RequestMapping("/listen")
    public DeferredResult<ListeningClientsContainer.ListenResult> listen(Set<ListenMeta> listenMetas) {
        // 查找需要立即刷新的配置主题
        ListeningClientsContainer.ListenResult listenResult = new ListeningClientsContainer.ListenResult();
        for (ListenMeta listenMeta : listenMetas) {
            FindPropertiesResult findPropertiesResult = findProperties(listenMeta.getTopic().getAppId(), listenMeta.getTopic().getAppId(), listenMeta.getTopic().getProfileId());
            FacadeUtils.assertSuccess(findPropertiesResult);
            if (!Objects.equals(listenMeta.getConfigVersion(), findPropertiesResult.getVersion())) {
                listenResult.addTopic(listenMeta.getTopic());
            }
        }
        // 构建异步返回结果
        DeferredResult<ListeningClientsContainer.ListenResult> deferredResult = new DeferredResult<>(90000L, new ListeningClientsContainer.ListenResult());
        if (listenMetas.isEmpty() || !listenResult.getTopics().isEmpty()) {
            // 直接设置返回结果
            deferredResult.setResult(listenResult);
        } else {
            // 监听刷新客户端事件
            Set<ConfigTopic> topics = listenMetas.stream().map(ListenMeta::getTopic).collect(Collectors.toSet());
            ListeningClientsContainer.ListeningClient listeningClient = new ListeningClientsContainer.ListeningClient(topics, deferredResult);
            listeningClientsContainer.addClient(listeningClient);
            deferredResult.onCompletion(() -> listeningClientsContainer.removeClient(listeningClient));
        }
        return deferredResult;
    }

    /**
     * 监听元数据
     */
    @AllArgsConstructor
    @Getter
    public final static class ListenMeta {
        // 监听的配置主题
        private ConfigTopic topic;
        // 配置的版本
        private Long configVersion;

        @Override
        public int hashCode() {
            return Objects.hash(topic, configVersion);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ListenMeta)) {
                return false;
            }
            ListenMeta other = (ListenMeta) obj;
            return Objects.equals(topic, other.topic) && Objects.equals(configVersion, other.configVersion);
        }

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }
}
