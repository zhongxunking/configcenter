/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 16:05 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.facade.info.AppTree;
import org.antframework.configcenter.facade.info.ProfileTree;
import org.antframework.configcenter.facade.order.RefreshClientsOrder;
import org.antframework.configcenter.facade.vo.ConfigTopic;
import org.antframework.configcenter.facade.vo.RedisConstant;
import org.antframework.configcenter.facade.vo.RefreshClientsEvent;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 刷新客户端服务
 */
@Service
@AllArgsConstructor
public class RefreshClientsService {
    // redis操作类
    private final RedisTemplate<Object, Object> redisTemplate;

    @ServiceExecute
    public void execute(ServiceContext<RefreshClientsOrder, EmptyResult> context) {
        RefreshClientsOrder order = context.getOrder();
        // 获取需要刷新的应用
        List<String> appIds = new ArrayList<>();
        extractAppIds(Apps.findAppTree(order.getRootAppId()), appIds);
        // 获取需要刷新的环境
        List<String> profileIds = new ArrayList<>();
        extractProfileIds(ProfileUtils.findProfileTree(order.getRootProfileId()), profileIds);
        // 构建刷新客户端事件
        Set<ConfigTopic> topics = new HashSet<>(appIds.size() * profileIds.size());
        for (String appId : appIds) {
            for (String profileId : profileIds) {
                topics.add(new ConfigTopic(appId, profileId));
            }
        }
        RefreshClientsEvent event = new RefreshClientsEvent(topics);
        // 发送事件
        redisTemplate.convertAndSend(RedisConstant.REFRESH_CLIENTS_CHANNEL, event);
    }

    // 提取出应用id
    private void extractAppIds(AppTree appTree, List<String> appIds) {
        if (appTree.getApp() != null) {
            appIds.add(appTree.getApp().getAppId());
        }
        for (AppTree child : appTree.getChildren()) {
            extractAppIds(child, appIds);
        }
    }

    // 提取出环境id
    private void extractProfileIds(ProfileTree profileTree, List<String> profileIds) {
        if (profileTree.getProfile() != null) {
            profileIds.add(profileTree.getProfile().getProfileId());
        }
        for (ProfileTree child : profileTree.getChildren()) {
            extractProfileIds(child, profileIds);
        }
    }
}
