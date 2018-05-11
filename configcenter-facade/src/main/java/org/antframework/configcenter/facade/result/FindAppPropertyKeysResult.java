/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:04 创建
 */
package org.antframework.configcenter.facade.result;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用的所有属性key-result
 */
public class FindAppPropertyKeysResult extends AbstractResult {
    // 属性key数据
    private List<PropertyKeyInfo> infos = new ArrayList<>();

    public List<PropertyKeyInfo> getInfos() {
        return infos;
    }

    public void addInfo(PropertyKeyInfo info) {
        infos.add(info);
    }
}
