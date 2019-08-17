/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:23 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.PropertyValueInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用在指定环境指定分支的所有配置value-result
 */
@Getter
public class FindAppProfilePropertyValuesResult extends AbstractResult {
    // 配置value
    private final List<PropertyValueInfo> propertyValues = new ArrayList<>();

    public void addPropertyValue(PropertyValueInfo propertyValue) {
        propertyValues.add(propertyValue);
    }
}
