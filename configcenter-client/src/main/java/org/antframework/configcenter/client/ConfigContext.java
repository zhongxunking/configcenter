/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 14:53 创建
 */
package org.antframework.configcenter.client;

import org.antframework.common.util.zookeeper.ZkTemplate;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class ConfigContext {
    private static final Logger logger = LoggerFactory.getLogger(ConfigContext.class);

    public static final String ZK_CONFIG_NAMESPACE = "configcenter/config";

    private String configCenterUrl;
    private String configZkUrl;
    private String configFilePath;
    private String profileCode;
    private String appCode;
    private String queriedAppCode;
    private ZkTemplate zkTemplate;
    private HttpClient httpClient;
    private ConfigProperties configProperties;
    private BlockingQueue queue = new LinkedBlockingQueue();

    public ConfigContext(String configCenterUrl, String configZkUrl, String configFilePath, String profileCode, String appCode, String queriedAppCode) {
        this.configCenterUrl = configCenterUrl;
        this.configZkUrl = configZkUrl;
        this.configFilePath = configFilePath;
        this.profileCode = profileCode;
        this.appCode = appCode;
        this.queriedAppCode = queriedAppCode;
    }

}
