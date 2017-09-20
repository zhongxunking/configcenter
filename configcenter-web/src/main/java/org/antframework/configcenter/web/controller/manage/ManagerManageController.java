/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 21:16 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.AbstractQueryResult;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.web.manager.facade.api.ManagerManageService;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;
import org.antframework.configcenter.web.manager.facade.info.ManagerInfo;
import org.antframework.configcenter.web.manager.facade.order.AddManagerOrder;
import org.antframework.configcenter.web.manager.facade.order.DeleteManagerOrder;
import org.antframework.configcenter.web.manager.facade.order.ModifyManagerTypeOrder;
import org.antframework.configcenter.web.manager.facade.order.QueryManagerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理管理员的controller
 */
@RestController
@RequestMapping("/manage/manager")
public class ManagerManageController {
    @Autowired
    private ManagerManageService managerManageService;

    /**
     * 添加管理员
     *
     * @param username 用户名（必须）
     * @param password 密码（必须）
     * @param type     类型（必须）
     */
    @RequestMapping("/addManager")
    public AbstractResult addManager(String username, String password, ManagerType type) {
        AddManagerOrder order = new AddManagerOrder();
        order.setUsername(username);
        order.setPassword(password);
        order.setType(type);

        return managerManageService.addManager(order);
    }

    /**
     * 删除管理员
     *
     * @param username 用户名（必须）
     */
    @RequestMapping("/deleteManager")
    public AbstractResult deleteManager(String username) {
        DeleteManagerOrder order = new DeleteManagerOrder();
        order.setUsername(username);

        return managerManageService.deleteManager(order);
    }

    /**
     * 修改管理员类型
     *
     * @param username 用户名（必须）
     * @param newType  新类型（必须）
     */
    @RequestMapping("/modifyManagerType")
    public AbstractResult modifyManagerType(String username, ManagerType newType) {
        ModifyManagerTypeOrder order = new ModifyManagerTypeOrder();
        order.setUsername(username);
        order.setNewType(newType);

        return managerManageService.modifyManagerType(order);
    }

    /**
     * 查询管理员
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param username 用户名（必须）
     * @param type     类型（必须）
     */
    @RequestMapping("/queryManager")
    public AbstractQueryResult<ManagerInfo> queryManager(int pageNo, int pageSize, String username, ManagerType type) {
        QueryManagerOrder order = new QueryManagerOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setUsername(username);
        order.setType(type);

        return managerManageService.queryManager(order);
    }
}
