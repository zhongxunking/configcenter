/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:23 创建
 */
package org.antframework.configcenter.facade.result.manage;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.PropertyValueInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用在指定环境的所有属性value-result
 */
public class FindAppProfilePropertyValueResult extends AbstractResult {
    // 属性value数据
    private List<PropertyValueInfo> infos = new ArrayList<>();

    public List<PropertyValueInfo> getInfos() {
        return infos;
    }

    public void addInfo(PropertyValueInfo info) {
        infos.add(info);
    }
}
