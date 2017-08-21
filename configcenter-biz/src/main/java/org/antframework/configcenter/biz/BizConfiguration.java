/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-06-22 13:00 创建
 */
package org.antframework.configcenter.biz;

import org.antframework.configcenter.biz.bekitboot.boot.BekitConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * biz层配置
 */
@Configuration
@Import(BekitConfiguration.class)
public class BizConfiguration {

}
