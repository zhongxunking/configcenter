/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 21:16 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.configcenter.web.manager.facade.api.ManagerManageService;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;
import org.antframework.configcenter.web.manager.facade.order.*;
import org.antframework.configcenter.web.manager.facade.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理管理员的controller
 */
@RestController
@RequestMapping("/manage/manager")
public class ManagerManageController extends AbstractController {
    @Autowired
    private ManagerManageService managerManageService;

    /**
     * 添加管理员
     *
     * @param code     编码（必须）
     * @param name     名称（必须）
     * @param password 密码（必须）
     * @param type     类型（必须）
     */
    @RequestMapping("/addManager")
    public AddManagerResult addManager(String code, String name, String password, ManagerType type) {
        assertAdmin();
        AddManagerOrder order = new AddManagerOrder();
        order.setCode(code);
        order.setName(name);
        order.setPassword(password);
        order.setType(type);

        return managerManageService.addManager(order);
    }

    /**
     * 删除管理员
     *
     * @param code 编码（必须）
     */
    @RequestMapping("/deleteManager")
    public DeleteManagerResult deleteManager(String code) {
        assertAdmin();
        DeleteManagerOrder order = new DeleteManagerOrder();
        order.setCode(code);

        return managerManageService.deleteManager(order);
    }

    /**
     * 修改管理员密码
     *
     * @param code        编码（必须）
     * @param newPassword 新密码（必须）
     */
    @RequestMapping("/modifyManagerPassword")
    public ModifyManagerPasswordResult modifyManagerPassword(String code, String newPassword) {
        assertAdminOrMyself(code);
        ModifyManagerPasswordOrder order = new ModifyManagerPasswordOrder();
        order.setCode(code);
        order.setNewPassword(newPassword);

        return managerManageService.modifyManagerPassword(order);
    }

    /**
     * 修改管理员类型
     *
     * @param code    编码（必须）
     * @param newType 新类型（必须）
     */
    @RequestMapping("/modifyManagerType")
    public ModifyManagerTypeResult modifyManagerType(String code, ManagerType newType) {
        assertAdmin();
        ModifyManagerTypeOrder order = new ModifyManagerTypeOrder();
        order.setCode(code);
        order.setNewType(newType);

        return managerManageService.modifyManagerType(order);
    }

    /**
     * 修改管理员名称
     *
     * @param code    编码
     * @param newName 新名称
     */
    @RequestMapping("/modifyManagerName")
    public ModifyManagerNameResult modifyManagerName(String code, String newName) {
        assertAdminOrMyself(code);
        ModifyManagerNameOrder order = new ModifyManagerNameOrder();
        order.setCode(code);
        order.setNewName(newName);

        return managerManageService.modifyManagerName(order);
    }

    /**
     * 查询管理员
     *
     * @param pageNo   页码（必须）
     * @param pageSize 每页大小（必须）
     * @param code     编码（必须）
     * @param name     名称（必须）
     * @param type     类型（必须）
     */
    @RequestMapping("/queryManager")
    public QueryManagerResult queryManager(int pageNo, int pageSize, String code, String name, ManagerType type) {
        assertAdmin();
        QueryManagerOrder order = new QueryManagerOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setCode(code);
        order.setName(name);
        order.setType(type);

        return managerManageService.queryManager(order);
    }
}
