/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 02:08 创建
 */
package org.antframework.configcenter.facade.api.manage;

import org.antframework.configcenter.facade.order.manage.AddOrModifyProfileOrder;
import org.antframework.configcenter.facade.order.manage.DeleteProfileOrder;
import org.antframework.configcenter.facade.order.manage.FindProfileOrder;
import org.antframework.configcenter.facade.order.manage.QueryProfileOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyProfileResult;
import org.antframework.configcenter.facade.result.manage.DeleteProfileResult;
import org.antframework.configcenter.facade.result.manage.FindProfileResult;
import org.antframework.configcenter.facade.result.manage.QueryProfileResult;

/**
 * 环境管理服务
 */
public interface ProfileManageService {
    /**
     * 添加或修改环境
     */
    AddOrModifyProfileResult addOrModifyProfile(AddOrModifyProfileOrder order);

    /**
     * 删除环境
     */
    DeleteProfileResult deleteProfile(DeleteProfileOrder order);

    /**
     * 查找环境
     */
    FindProfileResult findProfile(FindProfileOrder order);

    /**
     * 查询环境
     */
    QueryProfileResult queryProfile(QueryProfileOrder order);
}
