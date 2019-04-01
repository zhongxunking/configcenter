/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:44 创建
 */
package org.antframework.configcenter.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import org.antframework.configcenter.web.controller.ConfigController;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * web层配置
 */
@Configuration
@AllArgsConstructor
public class WebConfiguration {
    // 转换器注册器
    private final List<ConverterRegistry> converterRegistries;

    @PostConstruct
    public void init() {
        if (converterRegistries == null) {
            return;
        }
        // 注册转换器
        for (ConverterRegistry registry : converterRegistries) {
            registry.addConverter(new StringToSetListenMetaConverter());
        }
    }

    /**
     * String转Set<ListenMeta>转换器
     */
    public static class StringToSetListenMetaConverter implements Converter<String, Set<ConfigController.ListenMeta>> {
        @Override
        public Set<ConfigController.ListenMeta> convert(String source) {
            return JSON.parseObject(source, new TypeReference<Set<ConfigController.ListenMeta>>() {
            });
        }
    }
}
