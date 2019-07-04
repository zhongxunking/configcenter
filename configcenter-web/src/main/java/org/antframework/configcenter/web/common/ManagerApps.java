/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-11-10 21:42 创建
 */
package org.antframework.configcenter.web.common;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.*;
import org.antframework.manager.biz.util.Relations;
import org.antframework.manager.facade.api.RelationService;
import org.antframework.manager.facade.info.ManagerInfo;
import org.antframework.manager.facade.info.RelationInfo;
import org.antframework.manager.facade.order.QuerySourceRelationsOrder;
import org.antframework.manager.facade.result.QuerySourceRelationsResult;
import org.antframework.manager.web.CurrentManagers;
import org.springframework.beans.BeanUtils;

/**
 * 管理员与应用关系的工具类
 */
public final class ManagerApps {
    // 关系类型
    private static final String RELATION_TYPE = "manager-app";
    // 关系服务
    private static final RelationService RELATION_SERVICE = Contexts.getApplicationContext().getBean(RelationService.class);

    /**
     * 断言当前管理员为超级管理员或管理着指定应用
     *
     * @param appId 应用id
     */
    public static void adminOrHaveApp(String appId) {
        try {
            CurrentManagers.admin();
        } catch (BizException e) {
            ManagerInfo manager = CurrentManagers.current();
            RelationInfo relation = Relations.findRelation(RELATION_TYPE, manager.getManagerId(), appId);
            if (relation == null) {
                throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), CommonResultCode.ILLEGAL_STATE.getMessage());
            }
        }
    }

    /**
     * 删除与指定应用有关的管理权限
     *
     * @param appId 应用id
     */
    public static void deletesByApp(String appId) {
        Relations.deleteRelations(RELATION_TYPE, null, appId);
    }

    /**
     * 查找管理员管理的应用
     *
     * @param pageNo   页码
     * @param pageSize 每页大小
     * @param appId    应用id
     * @return 被管理的应用
     */
    public static AbstractQueryResult<String> queryManagedApps(int pageNo, int pageSize, String appId) {
        QuerySourceRelationsOrder order = new QuerySourceRelationsOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setType(RELATION_TYPE);
        order.setSource(CurrentManagers.current().getManagerId());
        order.setTarget(appId);

        QuerySourceRelationsResult relationsResult = RELATION_SERVICE.querySourceRelations(order);
        FacadeUtils.assertSuccess(relationsResult);

        AbstractQueryResult<String> result = new AbstractQueryResult<String>() {
        };
        BeanUtils.copyProperties(relationsResult, result, "infos");
        for (RelationInfo relation : relationsResult.getInfos()) {
            result.addInfo(relation.getTarget());
        }

        return result;
    }
}
