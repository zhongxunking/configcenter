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
import org.antframework.configcenter.client.ConfigContext;
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
    // 查询配置url
    private static final String QUERY_CONFIG_URL_SUFFIX = "/config/findProperties";
    // 查询元数据url
    private static final String QUERY_META_URL_SUFFIX = "/config/meta";

    // 获取配置的请求
    private HttpUriRequest configRequest;
    // 获取元数据的请求
    private HttpUriRequest metaRequest;

    public ServerRequester(ConfigContext.InitParams initParams) {
        configRequest = buildConfigRequest(initParams);
        metaRequest = buildMetaRequest(initParams);
    }

    /**
     * 查找配置
     */
    public Map<String, String> findConfig() {
        try {
            String resultStr = HTTP_CLIENT.execute(configRequest, new BasicResponseHandler());
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
    private HttpUriRequest buildConfigRequest(ConfigContext.InitParams initParams) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("mainAppId", initParams.getMainAppId()));
        params.add(new BasicNameValuePair("queriedAppId", initParams.getQueriedAppId()));
        params.add(new BasicNameValuePair("profileId", initParams.getProfileId()));

        HttpPost httpPost = new HttpPost(initParams.getServerUrl() + QUERY_CONFIG_URL_SUFFIX);
        httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("utf-8")));
        return httpPost;
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
     * 获取配置中心使用的zookeeper
     */
    public String[] getZkUrls() {
        try {
            String resultStr = HTTP_CLIENT.execute(metaRequest, new BasicResponseHandler());
            MetaResult result = JSON.parseObject(resultStr, MetaResult.class);
            if (result == null) {
                throw new RuntimeException("请求配置中心失败");
            }
            if (!result.isSuccess()) {
                throw new RuntimeException("从配置中心获取元数据失败：" + result.getMessage());
            }
            return result.getZkUrls();
        } catch (IOException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    // 构建元数据请求
    private HttpUriRequest buildMetaRequest(ConfigContext.InitParams initParams) {
        return new HttpGet(initParams.getServerUrl() + QUERY_META_URL_SUFFIX);
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
