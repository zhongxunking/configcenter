/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-04 22:17 创建
 */
package org.antframework.configcenter.dal.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.common.util.JSON;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 发布
 */
@Entity
@Table(name = "`Release`",
        uniqueConstraints = @UniqueConstraint(name = "uk_appId_profileId_version", columnNames = {"appId", "profileId", "version"}),
        indexes = @Index(name = "idx_appId_profileId_parentVersion", columnList = "appId,profileId,parentVersion"))
@Getter
@Setter
public class Release extends AbstractEntity {
    // 应用id
    @Column(length = 64)
    private String appId;

    // 环境id
    @Column(length = 64)
    private String profileId;

    // 版本
    @Column
    private Long version;

    // 发布时间
    @Column
    private Date releaseTime;

    // 备注
    @Column
    private String memo;

    // 配置集
    @Column(length = 1024 * 1024)
    @Convert(converter = PropertiesConverter.class)
    private Set<Property> properties;

    // 父版本
    @Column
    private Long parentVersion;

    // 配置集的jpa转换器
    private static class PropertiesConverter implements AttributeConverter<Set<Property>, String> {
        @Override
        public String convertToDatabaseColumn(Set<Property> attribute) {
            if (attribute == null) {
                return null;
            }
            try {
                return JSON.OBJECT_MAPPER.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                return ExceptionUtils.rethrow(e);
            }
        }

        @Override
        public Set<Property> convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            try {
                Set<PropertyInfo> properties = JSON.OBJECT_MAPPER.readValue(dbData, new TypeReference<Set<PropertyInfo>>() {
                });
                return properties.stream()
                        .map(property -> new Property(property.getKey(), property.getValue(), property.getScope()))
                        .collect(Collectors.toSet());
            } catch (JsonProcessingException e) {
                return ExceptionUtils.rethrow(e);
            }
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
}
