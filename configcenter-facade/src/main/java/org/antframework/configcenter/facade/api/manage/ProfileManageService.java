/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:08 创建
 */
package org.antframework.configcenter.facade.api.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.manage.AddOrModifyProfileOrder;
import org.antframework.configcenter.facade.order.manage.DeleteProfileOrder;
import org.antframework.configcenter.facade.order.manage.FindAllProfileOrder;
import org.antframework.configcenter.facade.order.manage.QueryProfileOrder;
import org.antframework.configcenter.facade.result.manage.DeleteProfileResult;
import org.antframework.configcenter.facade.result.manage.FindAllProfileResult;
import org.antframework.configcenter.facade.result.manage.QueryProfileResult;

/**
 * 环境管理服务
 */
public interface ProfileManageService {
    /**
     * 添加或修改环境
     */
    EmptyResult addOrModifyProfile(AddOrModifyProfileOrder order);

    /**
     * 删除环境
     */
    DeleteProfileResult deleteProfile(DeleteProfileOrder order);

    /**
     * 查找所有环境
     */
    FindAllProfileResult findAllProfile(FindAllProfileOrder order);

    /**
     * 查询环境
     */
    QueryProfileResult queryProfile(QueryProfileOrder order);
}
