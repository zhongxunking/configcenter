/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:44 创建
 */
package org.antframework.configcenter.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.json.JSON;
import org.antframework.configcenter.facade.info.PropertyChange;
import org.antframework.configcenter.facade.vo.ConfigTopic;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.configcenter.web.controller.ConfigController;
import org.antframework.manager.facade.event.ManagerDeletingEvent;
import org.antframework.manager.facade.info.ManagerInfo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bekit.event.annotation.DomainListener;
import org.bekit.event.annotation.Listen;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * web层配置
 */
@Configuration
@EnableConfigurationProperties(WebConfiguration.ConfigcenterProperties.class)
@Import(WebConfiguration.ManagerListener.class)
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
            registry.addConverter(new StringToPropertyChangeConverter());
        }
    }

    /**
     * String转Set&lt;ListenMeta&gt;转换器
     */
    public static class StringToSetListenMetaConverter implements Converter<String, Set<ConfigController.ListenMeta>> {
        @Override
        public Set<ConfigController.ListenMeta> convert(String source) {
            try {
                Set<ListenMetaInfo> listenMetas = JSON.OBJECT_MAPPER.readValue(source, new TypeReference<Set<ListenMetaInfo>>() {
                });
                return listenMetas.stream()
                        .map(listenMeta -> new ConfigController.ListenMeta(new ConfigTopic(listenMeta.getTopic().getAppId(), listenMeta.getTopic().getProfileId()), listenMeta.getConfigVersion()))
                        .collect(Collectors.toSet());
            } catch (JsonProcessingException e) {
                return ExceptionUtils.rethrow(e);
            }
        }

        // 监听元数据info
        @Getter
        @Setter
        private static class ListenMetaInfo {
            // 监听的配置主题
            private ConfigTopicInfo topic;
            // 配置的版本
            private Long configVersion;
        }

        // 配置主题info
        @Getter
        @Setter
        private static class ConfigTopicInfo {
            // 应用id
            private String appId;
            // 环境id
            private String profileId;
        }
    }

    /**
     * String转PropertyChange转换器
     */
    public static class StringToPropertyChangeConverter implements Converter<String, PropertyChange> {
        @Override
        public PropertyChange convert(String source) {
            try {
                PropertyChangeInfo propertyChangeInfo = JSON.OBJECT_MAPPER.readValue(source, PropertyChangeInfo.class);
                PropertyChange propertyChange = new PropertyChange();
                for (PropertyInfo propertyInfo : propertyChangeInfo.addedOrModifiedProperties) {
                    propertyChange.addAddedOrModifiedProperty(new Property(propertyInfo.getKey(), propertyInfo.getValue(), propertyInfo.getScope()));
                }
                for (String key : propertyChangeInfo.getDeletedKeys()) {
                    propertyChange.addDeletedKey(key);
                }
                return propertyChange;
            } catch (JsonProcessingException e) {
                return ExceptionUtils.rethrow(e);
            }
        }

        // 配置变动info
        @Getter
        @Setter
        private static class PropertyChangeInfo {
            // 添加或修改的配置
            private Set<PropertyInfo> addedOrModifiedProperties;
            // 删除的配置key
            private Set<String> deletedKeys;
        }

        // 配置项info
        @Getter
        @Setter
        private static class PropertyInfo {
            // key
            private String key;
            // value
            private String value;
            // 作用域
            private Scope scope;
        }
    }

    /**
     * 管理员监听器
     */
    @DomainListener
    public static class ManagerListener {
        // 监听管理员删除事件
        @Listen
        public void listenManagerDeletingEvent(ManagerDeletingEvent event) {
            ManagerInfo manager = event.getManager();
            ManagerApps.deletesByManager(manager.getManagerId());
        }
    }

    /**
     * configcenter配置
     */
    @ConfigurationProperties("configcenter")
    @Validated
    @Getter
    @Setter
    public static class ConfigcenterProperties {
        // 配置
        private Config config = new Config();

        /**
         * 配置
         */
        @Getter
        @Setter
        public static class Config {
            /**
             * 选填：获取配置是否需要管理员认证（默认为不需要）
             */
            private boolean fetchNeedManager = false;
        }
    }
}
