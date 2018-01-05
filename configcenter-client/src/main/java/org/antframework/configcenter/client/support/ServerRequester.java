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
    private static final String QUERY_CONFIG_SUFFIX_URL = "/config/findProperties";

    // 发送给服务端的请求
    private HttpUriRequest request;

    public ServerRequester(ConfigContext.InitParams initParams) {
        request = buildRequest(initParams);
    }

    /**
     * 查询配置
     */
    public Map<String, String> queryConfig() {
        try {
            String resultStr = HTTP_CLIENT.execute(request, new BasicResponseHandler());
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

    // 构建请求
    private HttpUriRequest buildRequest(ConfigContext.InitParams initParams) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appCode", initParams.getAppCode()));
        params.add(new BasicNameValuePair("queriedAppCode", initParams.getQueriedAppCode()));
        params.add(new BasicNameValuePair("profileCode", initParams.getProfileCode()));

        HttpPost httpPost = new HttpPost(initParams.getServerUrl() + QUERY_CONFIG_SUFFIX_URL);
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
}
