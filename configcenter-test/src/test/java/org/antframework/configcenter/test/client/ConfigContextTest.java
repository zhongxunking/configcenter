/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 14:30 创建
 */
package org.antframework.configcenter.test.client;

import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.client.ConfigListener;
import org.antframework.configcenter.client.core.ModifiedProperty;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
@Ignore
public class ConfigContextTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigContextTest.class);

    @Test
    public void testConfigContext() throws InterruptedException {
        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setProfileCode("dev");
        initParams.setAppCode("scbfund");
        initParams.setQueriedAppCode("scbfund");
        initParams.setServerUrl("http://localhost:8080");
        initParams.setCacheFilePath(System.getProperty("user.home") + "/var/config/scbfund.properties");
        initParams.setZkUrls("localhost:2181");
        ConfigContext configContext = new ConfigContext(initParams);
        configContext.getListenerRegistrar().register(new ConfigListener() {
            @Override
            public void configModified(List<ModifiedProperty> modifiedProperties) {
                logger.info("监听到配置更新：");
                for (ModifiedProperty modifiedProperty : modifiedProperties) {
                    logger.info(ToString.toString(modifiedProperty));
                }
            }
        });
        configContext.listenConfigModified();
        configContext.refreshConfig();
        Thread.sleep(200000);
        configContext.close();
    }

}
