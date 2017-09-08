/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:33 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.biz.ZkOperations;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.order.manage.AddOrModifyProfileOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyProfileResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 添加会修改环境服务
 */
@Service(enableTx = true)
public class AddOrModifyProfileService {
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private ZkOperations zkOperations;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyProfileOrder, AddOrModifyProfileResult> context) {
        AddOrModifyProfileOrder order = context.getOrder();

        Profile profile = profileDao.findLockByProfileCode(order.getProfileCode());
        if (profile == null) {
            profile = buildProfile(order);
        } else {
            profile.setMemo(order.getMemo());
        }
        profileDao.save(profile);
    }

    @ServiceAfter
    public void after(ServiceContext<AddOrModifyProfileOrder, AddOrModifyProfileResult> context) {
        AddOrModifyProfileOrder order = context.getOrder();

        zkOperations.createNodesByPath(ZkOperations.buildPath(order.getProfileCode()));
    }

    // 构建环境
    private Profile buildProfile(AddOrModifyProfileOrder addOrModifyProfileOrder) {
        Profile profile = new Profile();
        BeanUtils.copyProperties(addOrModifyProfileOrder, profile);
        return profile;
    }
}
