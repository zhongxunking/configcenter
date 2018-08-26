/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:42 创建
 */
package org.antframework.configcenter.biz;

import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.biz.util.RefreshUtils;
import org.antframework.configcenter.facade.vo.ZkConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Timer;
import java.util.TimerTask;

/**
 * biz层配置
 */
@Configuration
@EnableConfigurationProperties(MetaProperties.class)
public class BizConfiguration {
    // 刷新zookeeper任务的执行周期（单位：毫秒）
    private static final long REFRESH_ZK_TASK_PERIOD = 5 * 60 * 1000;

    @Autowired
    private MetaProperties metaProperties;

    // zookeeper操作类
    @Bean
    public ZkTemplate zkTemplate() {
        return ZkTemplate.create(metaProperties.getZkUrls().toArray(new String[0]), ZkConstant.ZK_CONFIG_NAMESPACE);
    }

    // 刷新zookeeper的定时器
    @Bean(destroyMethod = "cancel")
    public Timer refreshZkTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                RefreshUtils.refreshZk();
            }
        };

        Timer timer = new Timer("Timer-refreshZk", true);
        timer.schedule(task, REFRESH_ZK_TASK_PERIOD, REFRESH_ZK_TASK_PERIOD);

        return timer;
    }
}
