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
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ServerQuerier {
    private static final Logger logger = LoggerFactory.getLogger(ServerQuerier.class);

    private URI queryUri;
    private String profileCode;
    private String appCode;
    private String queriedAppCode;
    private CloseableHttpClient httpClient;

    public ServerQuerier(String serverUrl, String profileCode, String appCode, String queriedAppCode) {
        try {
            this.queryUri = new URI(serverUrl + "/queryProperties");
            this.profileCode = profileCode;
            this.appCode = appCode;
            this.queriedAppCode = queriedAppCode;
            this.httpClient = HttpClients.createDefault();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(String.format("服务端地址格式[%s]不对", serverUrl), e);
        }
    }

    public Map<String, String> queryProperties() throws IOException {
        HttpUriRequest request = buildRequest();
        logger.info("调用服务端查询配置，入参：{}", request);
        String resultStr = httpClient.execute(request, new BasicResponseHandler());
        logger.info("调用服务端查询配置，出参：{}", resultStr);
        QueryPropertiesResult result = JSON.parseObject(resultStr, QueryPropertiesResult.class);
        if (result == null) {
            return null;
        }
        return result.getProperties();
    }

    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            logger.error("关闭httpClient失败：", e);
        }
    }

    private HttpUriRequest buildRequest() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("profileCode", profileCode));
        params.add(new BasicNameValuePair("appCode", appCode));
        params.add(new BasicNameValuePair("queriedAppCode", queriedAppCode));

        HttpPost httpPost = new HttpPost(queryUri);
        httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("utf-8")));
        return httpPost;
    }


    private static class QueryPropertiesResult extends AbstractResult {
        private Map<String, String> properties;

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }
}
