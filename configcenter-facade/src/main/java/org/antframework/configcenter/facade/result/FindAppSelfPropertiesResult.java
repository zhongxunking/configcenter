/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-20 22:26 创建
 */
package org.antframework.configcenter.facade.result;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用自己的属性result
 */
public class FindAppSelfPropertiesResult extends AbstractResult {
    // 属性
    private List<Property> properties = new ArrayList<>();

    public List<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }
}
