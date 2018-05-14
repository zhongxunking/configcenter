/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:33 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.order.AddOrModifyProfileOrder;
import org.apache.zookeeper.CreateMode;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 添加或修改环境服务
 */
@Service(enableTx = true)
public class AddOrModifyProfileService {
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyProfileOrder, EmptyResult> context) {
        AddOrModifyProfileOrder order = context.getOrder();

        Profile profile = profileDao.findLockByProfileId(order.getProfileId());
        if (profile == null) {
            profile = new Profile();
        }
        BeanUtils.copyProperties(order, profile);

        profileDao.save(profile);
    }

    @ServiceAfter
    public void after(ServiceContext<AddOrModifyProfileOrder, EmptyResult> context) {
        AddOrModifyProfileOrder order = context.getOrder();
        // 创建环境节点
        zkTemplate.createNode(ZkTemplate.buildPath(order.getProfileId()), CreateMode.PERSISTENT);
        // 创建该环境节点下所有应用节点
        List<App> apps = appDao.findAll();
        for (App app : apps) {
            zkTemplate.createNode(ZkTemplate.buildPath(order.getProfileId(), app.getAppId()), CreateMode.PERSISTENT);
        }
    }
}
