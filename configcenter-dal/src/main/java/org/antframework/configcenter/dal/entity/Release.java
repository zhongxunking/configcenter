/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-04 22:17 创建
 */
package org.antframework.configcenter.dal.entity;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.Property;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 发布
 */
@Entity
@Table(name = "`Release`", uniqueConstraints = @UniqueConstraint(name = "uk_appId_profileId_version", columnNames = {"appId", "profileId", "version"}))
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

    /**
     * 配置集的jpa转换器
     */
    public static class PropertiesConverter implements AttributeConverter<Set<Property>, String> {
        @Override
        public String convertToDatabaseColumn(Set<Property> attribute) {
            if (attribute == null) {
                return null;
            }
            return JSON.toJSONString(attribute);
        }

        @Override
        public Set<Property> convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            return new HashSet<>(JSON.parseArray(dbData, Property.class));
        }
    }
}
