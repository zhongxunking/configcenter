/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 20:57 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.Property;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * 新增发布order
 */
public class AddReleaseOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    @Getter
    @Setter
    private String appId;
    // 环境id
    @NotBlank
    @Getter
    @Setter
    private String profileId;
    // 父版本
    @NotNull
    @Getter
    @Setter
    private Long parentVersion;
    // 备注
    @Getter
    @Setter
    private String memo;
    // 需添加或修改的配置
    @NotNull
    @Getter
    private Set<Property> addedOrModifiedProperties = new HashSet<>();
    // 需删除的配置
    @NotNull
    @Getter
    private Set<String> deletedPropertyKeys = new HashSet<>();

    public void addAddedOrModifiedProperty(Property property) {
        addedOrModifiedProperties.add(property);
    }

    public void addDeletedPropertyKey(String propertyKey) {
        deletedPropertyKeys.add(propertyKey);
    }
}
