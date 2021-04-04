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
import org.antframework.configcenter.facade.order.FindConfigOrder;
import org.antframework.configcenter.facade.result.FindConfigResult;
import org.antframework.configcenter.facade.vo.ConfigTopic;
import org.antframework.configcenter.web.WebConfiguration;
import org.antframework.configcenter.web.common.ListeningClientsContainer;
import org.antframework.configcenter.web.common.ManagerApps;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 配置controller
 */
@RestController
@RequestMapping("/config")
@AllArgsConstructor
public class ConfigController {
    // 监听配置的最小超时时间
    private static final int LISTEN_MIN_TIMEOUT = 60000;
    // 监听配置的最大超时时间
    private static final int LISTEN_MAX_TIMEOUT = 90000;
    // 随机数
    private static final Random RANDOM = new Random();
    // 超时的监听result
    private static final ListeningClientsContainer.ListenResult TIMEOUT_LISTEN_RESULT = FacadeUtils.buildSuccess(ListeningClientsContainer.ListenResult.class);

    // 配置
    private final WebConfiguration.ConfigcenterProperties properties;
    // 配置服务
    private final ConfigService configService;
    // 监听刷新事件的客户端的容器
    private final ListeningClientsContainer listeningClientsContainer;

    /**
     * 查找应用在指定环境中的配置
     *
     * @param mainAppId    主体应用id
     * @param queriedAppId 被查询配置的应用id
     * @param profileId    环境id
     * @param target       目标
     */
    @RequestMapping("/findConfig")
    public FindConfigResult findConfig(String mainAppId, String queriedAppId, String profileId, String target) {
        if (properties.getConfig().isFetchNeedManager()) {
            ManagerApps.assertAdminOrHaveApp(mainAppId);
        }
        FindConfigOrder order = buildFindConfigOrder(mainAppId, queriedAppId, profileId, target);
        return configService.findConfig(order);
    }

    /**
     * 监听刷新客户端事件
     *
     * @param listenMetas 监听元数据
     * @param target      目标
     */
    @RequestMapping("/listen")
    public DeferredResult<ListeningClientsContainer.ListenResult> listen(@RequestParam Set<ListenMeta> listenMetas, String target) {
        // 查找需要立即刷新的配置主题
        ListeningClientsContainer.ListenResult listenResult = FacadeUtils.buildSuccess(ListeningClientsContainer.ListenResult.class);
        for (ListenMeta listenMeta : listenMetas) {
            FindConfigOrder findConfigOrder = buildFindConfigOrder(listenMeta.getTopic().getAppId(), listenMeta.getTopic().getAppId(), listenMeta.getTopic().getProfileId(), target);
            FindConfigResult findConfigResult = configService.findConfig(findConfigOrder);
            FacadeUtils.assertSuccess(findConfigResult);
            if (!Objects.equals(listenMeta.getConfigVersion(), findConfigResult.getVersion())) {
                listenResult.addTopic(listenMeta.getTopic());
            }
        }
        // 构建异步返回结果
        DeferredResult<ListeningClientsContainer.ListenResult> deferredResult = new DeferredResult<>(generateTimeout(), TIMEOUT_LISTEN_RESULT);
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

    // 构建FindConfigOrder
    private FindConfigOrder buildFindConfigOrder(String mainAppId, String queriedAppId, String profileId, String target) {
        FindConfigOrder order = new FindConfigOrder();
        order.setMainAppId(mainAppId);
        order.setQueriedAppId(queriedAppId);
        order.setProfileId(profileId);
        order.setTarget(target);

        return order;
    }

    // 生成超时时间
    private long generateTimeout() {
        return LISTEN_MIN_TIMEOUT + RANDOM.nextInt(LISTEN_MAX_TIMEOUT - LISTEN_MIN_TIMEOUT);
    }

    /**
     * 监听元数据
     */
    @AllArgsConstructor
    @Getter
    public static final class ListenMeta {
        // 监听的配置主题
        private final ConfigTopic topic;
        // 配置的版本
        private final Long configVersion;

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
