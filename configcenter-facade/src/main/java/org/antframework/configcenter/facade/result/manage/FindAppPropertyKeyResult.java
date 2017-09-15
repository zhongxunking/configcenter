/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:04 创建
 */
package org.antframework.configcenter.facade.result.manage;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;

import java.util.List;

/**
 * 查找应用的所有属性key-result
 */
public class FindAppPropertyKeyResult extends AbstractResult {
    // 属性key数据
    private List<PropertyKeyInfo> infos;

    public List<PropertyKeyInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<PropertyKeyInfo> infos) {
        this.infos = infos;
    }
}
