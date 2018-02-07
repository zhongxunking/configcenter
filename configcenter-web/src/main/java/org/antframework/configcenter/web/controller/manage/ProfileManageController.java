/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:00 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.api.ProfileService;
import org.antframework.configcenter.facade.order.AddOrModifyProfileOrder;
import org.antframework.configcenter.facade.order.DeleteProfileOrder;
import org.antframework.configcenter.facade.order.FindAllProfileOrder;
import org.antframework.configcenter.facade.order.QueryProfileOrder;
import org.antframework.configcenter.facade.result.FindAllProfileResult;
import org.antframework.configcenter.facade.result.QueryProfileResult;
import org.antframework.manager.web.common.ManagerAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 环境管理controller
 */
@RestController
@RequestMapping("/manage/profile")
public class ProfileManageController {
    @Autowired
    private ProfileService profileService;

    /**
     * 新增或修改环境
     *
     * @param profileCode 环境编码（必须）
     * @param memo        备注（可选）
     */
    @RequestMapping("/addOrModifyProfile")
    public EmptyResult addOrModifyProfile(String profileCode, String memo) {
        ManagerAssert.admin();
        AddOrModifyProfileOrder order = new AddOrModifyProfileOrder();
        order.setProfileCode(profileCode);
        order.setMemo(memo);

        return profileService.addOrModifyProfile(order);
    }

    /**
     * 删除环境
     *
     * @param profileCode 环境编码（必须）
     */
    @RequestMapping("/deleteProfile")
    public EmptyResult deleteProfile(String profileCode) {
        ManagerAssert.admin();
        DeleteProfileOrder order = new DeleteProfileOrder();
        order.setProfileCode(profileCode);

        return profileService.deleteProfile(order);
    }

    /**
     * 查找所有环境
     */
    @RequestMapping("/findAllProfile")
    public FindAllProfileResult findAllProfile() {
        ManagerAssert.currentManager();
        return profileService.findAllProfile(new FindAllProfileOrder());
    }

    /**
     * 分页查询环境
     *
     * @param pageNo      页码（必须）
     * @param pageSize    每页大小（必须）
     * @param profileCode 环境编码（可选，有值会进行模糊查询）
     */
    @RequestMapping("/queryProfile")
    public QueryProfileResult queryProfile(int pageNo, int pageSize, String profileCode) {
        ManagerAssert.currentManager();
        QueryProfileOrder order = new QueryProfileOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setProfileCode(profileCode);

        return profileService.queryProfile(order);
    }
}
