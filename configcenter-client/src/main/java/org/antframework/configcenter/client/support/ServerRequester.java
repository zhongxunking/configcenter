/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-12 19:10 创建
 */
package org.antframework.configcenter.client.support;

import com.alibaba.fastjson.JSON;
import org.antframework.common.util.facade.AbstractResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务端请求器
 */
public class ServerRequester {
    // 发送http请求的客户端
    private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();
    // 查询配置的url
    private static final String QUERY_CONFIG_URL_SUFFIX = "/config/findProperties";
    // 查询元数据的url
    private static final String QUERY_META_URL_SUFFIX = "/config/meta";

    // 服务端地址
    private final String serverUrl;
    // 主应用id
    private final String mainAppId;
    // 环境id
    private final String profileId;

    public ServerRequester(String serverUrl, String mainAppId, String profileId) {
        this.serverUrl = serverUrl;
        this.mainAppId = mainAppId;
        this.profileId = profileId;
    }

    /**
     * 创建配置请求器
     *
     * @param queriedAppId 被查询配置的应用id
     * @return 配置请求器
     */
    public ConfigRequester createConfigRequester(String queriedAppId) {
        return new ConfigRequester(queriedAppId);
    }

    /**
     * 配置请求器
     */
    public class ConfigRequester {
        // 被查询配置的应用id
        private final String queriedAppId;

        public ConfigRequester(String queriedAppId) {
            this.queriedAppId = queriedAppId;
        }

        /**
         * 查找配置
         */
        public Map<String, String> findConfig() {
            try {
                String resultStr = HTTP_CLIENT.execute(buildConfigRequest(), new BasicResponseHandler());
                FindPropertiesResult result = JSON.parseObject(resultStr, FindPropertiesResult.class);
                if (result == null) {
                    throw new RuntimeException("请求配置中心失败");
                }
                if (!result.isSuccess()) {
                    throw new RuntimeException("从配置中心读取配置失败：" + result.getMessage());
                }
                return result.getProperties();
            } catch (IOException e) {
                return ExceptionUtils.rethrow(e);
            }
        }

        // 构建配置请求
        private HttpUriRequest buildConfigRequest() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("mainAppId", mainAppId));
            params.add(new BasicNameValuePair("queriedAppId", queriedAppId));
            params.add(new BasicNameValuePair("profileId", profileId));

            HttpPost httpPost = new HttpPost(serverUrl + QUERY_CONFIG_URL_SUFFIX);
            httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("utf-8")));
            return httpPost;
        }
    }

    // 查找应用在特定环境中的配置result
    private static class FindPropertiesResult extends AbstractResult {
        // 属性
        private Map<String, String> properties;

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }

    /**
     * 创建元数据请求器
     *
     * @return 元数据请求器
     */
    public MetaRequester createMetaRequester() {
        return new MetaRequester();
    }

    /**
     * 元数据请求器
     */
    public class MetaRequester {
        /**
         * 获取配置中心使用的zookeeper地址
         */
        public String[] getZkUrls() {
            try {
                String resultStr = HTTP_CLIENT.execute(buildMetaRequest(), new BasicResponseHandler());
                MetaResult result = JSON.parseObject(resultStr, MetaResult.class);
                if (result == null) {
                    throw new RuntimeException("请求配置中心失败");
                }
                if (!result.isSuccess()) {
                    throw new RuntimeException("从配置中心获取zookeeper地址失败：" + result.getMessage());
                }
                return result.getZkUrls();
            } catch (IOException e) {
                return ExceptionUtils.rethrow(e);
            }
        }

        // 构建元数据请求
        private HttpUriRequest buildMetaRequest() {
            return new HttpGet(serverUrl + QUERY_META_URL_SUFFIX);
        }
    }

    // 元数据result
    private static class MetaResult extends AbstractResult {
        // 配置中心使用的zookeeper地址
        private String[] zkUrls;

        public String[] getZkUrls() {
            return zkUrls;
        }

        public void setZkUrls(String[] zkUrls) {
            this.zkUrls = zkUrls;
        }
    }
}
