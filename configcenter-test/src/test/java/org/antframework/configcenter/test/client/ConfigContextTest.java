/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 14:30 创建
 */
package org.antframework.configcenter.test.client;

import org.antframework.configcenter.client.ConfigContext;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
@Ignore
public class ConfigContextTest {

    @Test
    public void testConfigContext() throws InterruptedException {
        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setProfileCode("dev");
        initParams.setAppCode("scbfund");
        initParams.setQueriedAppCode("scbfund");
        initParams.setServerUrl("http://localhost:8080");
        initParams.setCacheFilePath("/aa/config/scbfund.properties");
        initParams.setZkUrl("localhost:2181");
        ConfigContext configContext = new ConfigContext(initParams);
        configContext.listenConfigModified();
        configContext.refreshConfig();
        Thread.sleep(200000);
        configContext.close();
    }

}
