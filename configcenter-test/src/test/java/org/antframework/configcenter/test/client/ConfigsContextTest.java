/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 14:30 创建
 */
package org.antframework.configcenter.test.client;

import org.antframework.configcenter.client.Config;
import org.antframework.configcenter.client.ConfigListener;
import org.antframework.configcenter.client.ConfigsContext;
import org.antframework.configcenter.client.core.ChangedProperty;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 配置上下文单元测试
 */
@Ignore
public class ConfigsContextTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigsContextTest.class);

    @Test
    public void testConfigsContext_withCache() throws InterruptedException {
        testConfigsContext(System.getProperty("user.home") + "/var/config");
    }

    @Test
    public void testConfigsContext_withoutCache() throws InterruptedException {
        testConfigsContext(null);
    }

    private void testConfigsContext(String cacheDir) {
        ConfigsContext configsContext = new ConfigsContext(
                "customer",
                "dev",
                null,
                "http://localhost:6220",
                cacheDir);
        Config customerConfig = configsContext.getConfig("customer");
        customerConfig.getListeners().addListener(new ConfigListener() {
            @Override
            public void onChange(List<ChangedProperty> changedProperties) {
                for (ChangedProperty changedProperty : changedProperties) {
                    logger.info("监听到配置更新：{}", changedProperty);
                }
            }
        });
        Config accountConfig = configsContext.getConfig("account");
        accountConfig.getListeners().addListener(new ConfigListener() {
            @Override
            public void onChange(List<ChangedProperty> changedProperties) {
                for (ChangedProperty changedProperty : changedProperties) {
                    logger.info("监听到配置更新：{}", changedProperty);
                }
            }
        });
        configsContext.listenServer();
        configsContext.refresh();
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            ExceptionUtils.rethrow(e);
        }
        configsContext.close();
    }
}
