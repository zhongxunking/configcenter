/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-23 17:52 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.AddReleaseResult;
import org.antframework.configcenter.facade.result.FindCurrentReleaseResult;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.result.QueryReleasesResult;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.manager.web.Managers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发布controller
 */
@RestController
@RequestMapping("/manage/release")
public class ReleaseController {
    @Autowired
    private ReleaseService releaseService;

    /**
     * 新增发布
     *
     * @param appId     应用id（必须）
     * @param profileId 环境id（必须）
     * @param memo      备注（可选）
     */
    @RequestMapping("/addRelease")
    public AddReleaseResult addRelease(String appId, String profileId, String memo) {
        ManagerApps.adminOrHaveApp(appId);
        AddReleaseOrder order = new AddReleaseOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setMemo(memo);

        return releaseService.addRelease(order);
    }

    /**
     * 回滚发布
     *
     * @param appId         应用id（必须）
     * @param profileId     环境id（必须）
     * @param targetVersion 回滚到的目标版本（必须）
     */
    @RequestMapping("/revertRelease")
    public EmptyResult revertRelease(String appId, String profileId, Long targetVersion) {
        ManagerApps.adminOrHaveApp(appId);
        RevertReleaseOrder order = new RevertReleaseOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setTargetVersion(targetVersion);

        return releaseService.revertRelease(order);
    }

    /**
     * 查找当前发布
     *
     * @param appId     应用id（必须）
     * @param profileId 环境id（必须）
     */
    @RequestMapping("/findCurrentRelease")
    public FindCurrentReleaseResult findCurrentRelease(String appId, String profileId) {
        ManagerApps.adminOrHaveApp(appId);
        FindCurrentReleaseOrder order = new FindCurrentReleaseOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        return releaseService.findCurrentRelease(order);
    }

    /**
     * 查找发布
     *
     * @param appId     应用id（必须）
     * @param profileId 环境id（必须）
     * @param version   版本（必须）
     */
    @RequestMapping("/findRelease")
    public FindReleaseResult findRelease(String appId, String profileId, Long version) {
        ManagerApps.adminOrHaveApp(appId);
        FindReleaseOrder order = new FindReleaseOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setVersion(version);

        return releaseService.findRelease(order);
    }

    /**
     * 查询发布
     *
     * @param pageNo    页码（必须）
     * @param pageSize  每页大小（必须）
     * @param appId     应用id（可选）
     * @param profileId 环境id（可选）
     */
    @RequestMapping("/queryReleases")
    public QueryReleasesResult queryReleases(int pageNo, int pageSize, String appId, String profileId) {
        if (appId == null) {
            Managers.admin();
        } else {
            ManagerApps.adminOrHaveApp(appId);
        }
        QueryReleasesOrder order = new QueryReleasesOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppId(appId);
        order.setProfileId(profileId);

        return releaseService.queryReleases(order);
    }
}
