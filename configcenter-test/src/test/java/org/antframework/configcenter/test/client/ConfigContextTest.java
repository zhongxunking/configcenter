/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 14:30 创建
 */
package org.antframework.configcenter.test.client;

import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.client.ConfigListener;
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
public class ConfigContextTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigContextTest.class);

    @Test
    public void testConfigContext_withCache() throws InterruptedException {
        testConfigContext(System.getProperty("user.home") + "/var/config");
    }

    @Test
    public void testConfigContext_withoutCache() throws InterruptedException {
        testConfigContext(null);
    }

    private void testConfigContext(String cacheDir) {
        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setServerUrl("http://localhost:6220");
        initParams.setMainAppId("scbfund");
        initParams.setProfileId("dev");
        initParams.setCacheDir(cacheDir);

        ConfigContext configContext = new ConfigContext(initParams);
        configContext.getConfig("scbfund").getListenerRegistrar().register(new ConfigListener() {
            @Override
            public void onChange(List<ChangedProperty> changedProperties) {
                for (ChangedProperty changedProperty : changedProperties) {
                    logger.info("监听到配置更新：{}", changedProperty);
                }
            }
        });
        configContext.listenConfigChanged();
        configContext.refresh();
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            ExceptionUtils.rethrow(e);
        }
        configContext.close();
    }
}
