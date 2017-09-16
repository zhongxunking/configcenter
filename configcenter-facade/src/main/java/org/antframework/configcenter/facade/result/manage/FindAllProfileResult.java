/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 20:18 创建
 */
package org.antframework.configcenter.facade.result.manage;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.ProfileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找所有环境result
 */
public class FindAllProfileResult extends AbstractResult {
    // 环境信息
    private List<ProfileInfo> infos = new ArrayList<>();

    public List<ProfileInfo> getInfos() {
        return infos;
    }

    public void addInfo(ProfileInfo info) {
        infos.add(info);
    }
}
