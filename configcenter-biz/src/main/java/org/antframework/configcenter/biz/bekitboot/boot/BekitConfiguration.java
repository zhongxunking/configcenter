/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:59 创建
 */
package org.antframework.configcenter.biz.bekitboot.boot;

import org.antframework.configcenter.biz.bekitboot.servicelistener.HolderClearServiceListener;
import org.antframework.configcenter.biz.bekitboot.servicelistener.LogPrintServiceListener;
import org.antframework.configcenter.biz.bekitboot.servicelistener.OrderValidateServiceListener;
import org.antframework.configcenter.biz.bekitboot.servicelistener.ResultMaintainServiceListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * bekit配置类
 */
@Configuration
public class BekitConfiguration {

    // 日志打印-服务监听器
    @Bean
    public LogPrintServiceListener logPrintServiceListener() {
        return new LogPrintServiceListener();
    }

    // 持有器清理-服务监听器
    @Bean
    public HolderClearServiceListener holderClearServiceListener() {
        return new HolderClearServiceListener();
    }

    // result维护-服务监听器
    @Bean
    public ResultMaintainServiceListener resultMaintainServiceListener() {
        return new ResultMaintainServiceListener();
    }

    // order校验-服务监听器
    @Bean
    public OrderValidateServiceListener orderValidateServiceListener() {
        return new OrderValidateServiceListener();
    }

}
