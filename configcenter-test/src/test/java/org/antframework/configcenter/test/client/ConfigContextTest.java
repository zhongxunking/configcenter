///*
// * 作者：钟勋 (e-mail:zhongxunking@163.com)
// */
//
///*
// * 修订记录:
// * @author 钟勋 2017-09-14 14:30 创建
// */
//package org.antframework.configcenter.test.client;
//
//import org.antframework.configcenter.client.ConfigsContext;
//import org.antframework.configcenter.client.ConfigListener;
//import org.antframework.configcenter.client.core.ChangedProperty;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
///**
// * 配置上下文单元测试
// */
//@Ignore
//public class ConfigContextTest {
//    private static final Logger logger = LoggerFactory.getLogger(ConfigContextTest.class);
//
//    @Test
//    public void testConfigContext() throws InterruptedException {
//        ConfigsContext.InitParams initParams = new ConfigsContext.InitParams();
//        initParams.setProfileId("dev");
//        initParams.setMainAppId("scbfund");
//        initParams.setQueriedAppId("scbfund");
//        initParams.setServerUrl("http://localhost:6220");
//        initParams.setCacheFile(System.getProperty("user.home") + "/var/config/scbfund.properties");
//
//        ConfigsContext configContext = new ConfigsContext(initParams);
//        configContext.getListenerRegistrar().register(new ConfigListener() {
//            @Override
//            public void onChange(List<ChangedProperty> changedProperties) {
//                for (ChangedProperty changedProperty : changedProperties) {
//                    logger.info("监听到配置更新：{}", changedProperty);
//                }
//            }
//        });
//        configContext.listenConfigChanged();
//        configContext.refresh();
//        Thread.sleep(200000);
//        configContext.close();
//    }
//
//    @Test
//    public void testConfigContext_noCacheFile() throws InterruptedException {
//        ConfigsContext.InitParams initParams = new ConfigsContext.InitParams();
//        initParams.setProfileId("dev");
//        initParams.setMainAppId("scbfund");
//        initParams.setQueriedAppId("scbfund");
//        initParams.setServerUrl("http://localhost:8080");
//
//        ConfigsContext configContext = new ConfigsContext(initParams);
//        configContext.getListenerRegistrar().register(new ConfigListener() {
//            @Override
//            public void onChange(List<ChangedProperty> changedProperties) {
//                logger.info("监听到配置更新：");
//                for (ChangedProperty changedProperty : changedProperties) {
//                    logger.info("监听到配置更新：{}", changedProperty);
//                }
//            }
//        });
//        configContext.listenConfigChanged();
//        configContext.refresh();
//        Thread.sleep(200000);
//        configContext.close();
//    }
//}
