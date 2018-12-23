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
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.FindInheritedProfilesResult;
import org.antframework.configcenter.facade.result.FindProfileResult;
import org.antframework.configcenter.facade.result.FindProfileTreeResult;
import org.antframework.configcenter.facade.result.QueryProfilesResult;
import org.antframework.manager.web.Managers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 环境管理controller
 */
@RestController
@RequestMapping("/manage/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    /**
     * 新增或修改环境
     *
     * @param profileId   环境id（必须）
     * @param profileName 环境名（可选）
     * @param parent      父环境id（可选）
     */
    @RequestMapping("/addOrModifyProfile")
    public EmptyResult addOrModifyProfile(String profileId, String profileName, String parent) {
        Managers.admin();
        AddOrModifyProfileOrder order = new AddOrModifyProfileOrder();
        order.setProfileId(profileId);
        order.setProfileName(profileName);
        order.setParent(parent);

        return profileService.addOrModifyProfile(order);
    }

    /**
     * 删除环境
     *
     * @param profileId 环境id（必须）
     */
    @RequestMapping("/deleteProfile")
    public EmptyResult deleteProfile(String profileId) {
        Managers.admin();
        DeleteProfileOrder order = new DeleteProfileOrder();
        order.setProfileId(profileId);

        return profileService.deleteProfile(order);
    }

    /**
     * 查找环境
     *
     * @param profileId 环境id（必填）
     */
    @RequestMapping("/findProfile")
    public FindProfileResult findProfile(String profileId) {
        Managers.currentManager();
        FindProfileOrder order = new FindProfileOrder();
        order.setProfileId(profileId);

        return profileService.findProfile(order);
    }

    /**
     * 查找环境继承的所有环境
     *
     * @param profileId 环境id（必填）
     */
    @RequestMapping("/findInheritedProfiles")
    public FindInheritedProfilesResult findInheritedProfiles(String profileId) {
        Managers.currentManager();
        FindInheritedProfilesOrder order = new FindInheritedProfilesOrder();
        order.setProfileId(profileId);

        return profileService.findInheritedProfiles(order);
    }

    /**
     * 查找环境树
     *
     * @param profileId 根节点环境id（不填表示查找所有环境）
     */
    @RequestMapping("/findProfileTree")
    public FindProfileTreeResult findProfileTree(String profileId) {
        Managers.currentManager();
        FindProfileTreeOrder order = new FindProfileTreeOrder();
        order.setProfileId(profileId);

        return profileService.findProfileTree(order);
    }

    /**
     * 分页查询环境
     *
     * @param pageNo    页码（必须）
     * @param pageSize  每页大小（必须）
     * @param profileId 环境id（可选）
     */
    @RequestMapping("/queryProfiles")
    public QueryProfilesResult queryProfiles(int pageNo, int pageSize, String profileId) {
        Managers.currentManager();
        QueryProfilesOrder order = new QueryProfilesOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setProfileId(profileId);

        return profileService.queryProfiles(order);
    }
}
