/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-12 19:10 创建
 */
package org.antframework.configcenter.client.support;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.tostring.ToString;
import org.antframework.common.util.tostring.format.HideDetail;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务端请求器
 */
@AllArgsConstructor
public class ServerRequester {
    // 发送http请求的客户端
    private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();
    // 查找配置的uri
    private static final String FIND_CONFIG_URI = "/config/findConfig";
    // 监听配置的uri
    private static final String LISTEN_URI = "/config/listen";

    // 主应用id
    private final String mainAppId;
    // 环境id
    private final String profileId;
    // 服务端地址
    private final String serverUrl;

    /**
     * 创建配置请求器
     *
     * @param queriedAppId 被查询配置的应用id
     * @param versionKey   配置版本的key
     * @return 配置请求器
     */
    public ConfigRequester createConfigRequester(String queriedAppId, String versionKey) {
        return new ConfigRequester(queriedAppId, versionKey);
    }

    /**
     * 配置请求器
     */
    @AllArgsConstructor
    public class ConfigRequester {
        // 被查询配置的应用id
        private final String queriedAppId;
        // 配置版本的key
        private final String versionKey;

        /**
         * 查找配置
         */
        public Map<String, String> findConfig() {
            try {
                String resultStr = HTTP_CLIENT.execute(buildRequest(), new BasicResponseHandler());
                FindConfigResult result = JSON.parseObject(resultStr, FindConfigResult.class);
                if (result == null) {
                    throw new RuntimeException("请求配置中心失败");
                }
                if (!result.isSuccess()) {
                    throw new RuntimeException("从配置中心读取配置失败：" + result.getMessage());
                }
                if (result.getProperties().containsKey(versionKey)) {
                    throw new RuntimeException("配置中心的配置不能包含：" + versionKey);
                }
                Map<String, String> config = new HashMap<>(result.getProperties());
                config.put(versionKey, result.getVersion().toString());
                return config;
            } catch (IOException e) {
                return ExceptionUtils.rethrow(e);
            }
        }

        // 构建请求
        private HttpUriRequest buildRequest() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("mainAppId", mainAppId));
            params.add(new BasicNameValuePair("queriedAppId", queriedAppId));
            params.add(new BasicNameValuePair("profileId", profileId));

            HttpPost httpPost = new HttpPost(serverUrl + FIND_CONFIG_URI);
            httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("utf-8")));
            return httpPost;
        }
    }

    // 查找应用在指定环境中的配置result
    @Getter
    @Setter
    private static class FindConfigResult extends AbstractResult {
        // 版本
        private Long version;
        // 配置
        @HideDetail
        private Map<String, String> properties;
    }

    /**
     * 创建监听请求器
     *
     * @return 监听请求器
     */
    public ListenRequester createListenRequester() {
        return new ListenRequester();
    }

    /**
     * 监听请求器
     */
    public class ListenRequester {
        /**
         * 监听配置
         *
         * @param appIds         被监听的应用id
         * @param configVersions 当前的配置版本
         * @return 配置有变更的应用id
         */
        public Set<String> listen(List<String> appIds, List<Long> configVersions) {
            try {
                String resultStr = HTTP_CLIENT.execute(buildRequest(appIds, configVersions), new BasicResponseHandler());
                ListenResult result = JSON.parseObject(resultStr, ListenResult.class);
                if (result == null) {
                    throw new RuntimeException("请求配置中心失败");
                }
                if (!result.isSuccess()) {
                    throw new RuntimeException("监听配置中心的配置失败：" + result.getMessage());
                }
                return result.getTopics().stream().map(ConfigTopic::getAppId).collect(Collectors.toSet());
            } catch (IOException e) {
                return ExceptionUtils.rethrow(e);
            }
        }

        // 构建请求
        private HttpUriRequest buildRequest(List<String> appIds, List<Long> configVersions) {
            Set<ListenMeta> listenMetas = new HashSet<>(appIds.size());
            for (int i = 0; i < appIds.size(); i++) {
                String appId = appIds.get(i);
                long configVersion = configVersions.get(i);
                ListenMeta listenMeta = new ListenMeta(new ConfigTopic(appId, profileId), configVersion);
                listenMetas.add(listenMeta);
            }

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("listenMetas", JSON.toJSONString(listenMetas)));

            HttpPost httpPost = new HttpPost(serverUrl + LISTEN_URI);
            httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("utf-8")));
            return httpPost;
        }
    }

    /**
     * 监听元数据
     */
    @AllArgsConstructor
    @Getter
    private static final class ListenMeta {
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

    /**
     * 监听结果
     */
    @Getter
    private static class ListenResult extends AbstractResult {
        // 需客户端刷新的配置主题
        private final Set<ConfigTopic> topics = new HashSet<>();

        public void addTopic(ConfigTopic topic) {
            topics.add(topic);
        }
    }

    /**
     * 配置主题
     */
    @AllArgsConstructor
    @Getter
    private static final class ConfigTopic implements Serializable {
        // 应用id
        private final String appId;
        // 环境id
        private final String profileId;

        @Override
        public int hashCode() {
            return Objects.hash(appId, profileId);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ConfigTopic)) {
                return false;
            }
            ConfigTopic other = (ConfigTopic) obj;
            return Objects.equals(appId, other.appId) && Objects.equals(profileId, other.profileId);
        }

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }
}
