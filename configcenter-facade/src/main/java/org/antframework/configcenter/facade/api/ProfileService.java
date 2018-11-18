/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:08 创建
 */
package org.antframework.configcenter.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.FindInheritedProfilesResult;
import org.antframework.configcenter.facade.result.FindProfileResult;
import org.antframework.configcenter.facade.result.FindProfileTreeResult;
import org.antframework.configcenter.facade.result.QueryProfilesResult;

/**
 * 环境服务
 */
public interface ProfileService {
    /**
     * 添加或修改环境
     */
    EmptyResult addOrModifyProfile(AddOrModifyProfileOrder order);

    /**
     * 删除环境
     */
    EmptyResult deleteProfile(DeleteProfileOrder order);

    /**
     * 查找环境
     */
    FindProfileResult findProfile(FindProfileOrder order);

    /**
     * 查找环境继承的所有环境
     */
    FindInheritedProfilesResult findInheritedProfiles(FindInheritedProfilesOrder order);

    /**
     * 查找环境树
     */
    FindProfileTreeResult findProfileTree(FindProfileTreeOrder order);

    /**
     * 查询环境
     */
    QueryProfilesResult queryProfiles(QueryProfilesOrder order);
}
