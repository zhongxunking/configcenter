/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 13:56 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.apache.zookeeper.CreateMode;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步所有环境、应用到zookeeper服务
 */
@Service
public class SyncDataToZkService {
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<EmptyOrder, EmptyResult> context) {
        // 同步环境
        List<String> profileIds = new ArrayList<>();
        for (Profile profile : profileDao.findAll()) {
            profileIds.add(profile.getProfileId());
        }
        sync(ZkTemplate.buildPath(), profileIds);
        // 同步每个环境的应用
        List<String> appIds = new ArrayList<>();
        for (App app : appDao.findAll()) {
            appIds.add(app.getAppId());
        }
        for (String profileId : profileIds) {
            sync(ZkTemplate.buildPath(profileId), appIds);
        }
    }

    // 同步指定节点路径下子节点
    private void sync(String path, List<String> newChildren) {
        List<String> oldChildren = zkTemplate.getChildren(path);
        for (String child : newChildren) {
            zkTemplate.createNode(ZkTemplate.buildPath(path, child), CreateMode.PERSISTENT);
            oldChildren.remove(child);
        }
        for (String child : oldChildren) {
            zkTemplate.deleteNode(ZkTemplate.buildPath(path, child));
        }
    }
}
