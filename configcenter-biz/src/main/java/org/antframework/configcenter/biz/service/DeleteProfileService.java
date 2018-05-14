/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:40 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.order.DeleteProfileOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除环境服务
 */
@Service(enableTx = true)
public class DeleteProfileService {
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private PropertyValueDao propertyValueDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<DeleteProfileOrder, EmptyResult> context) {
        DeleteProfileOrder order = context.getOrder();

        Profile profile = profileDao.findLockByProfileId(order.getProfileId());
        if (profile == null) {
            return;
        }
        if (propertyValueDao.existsByProfileId(order.getProfileId())) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("环境[%s]还存在属性值，不能删除", order.getProfileId()));
        }

        profileDao.delete(profile);
    }

    @ServiceAfter
    public void after(ServiceContext<DeleteProfileOrder, EmptyResult> context) {
        DeleteProfileOrder order = context.getOrder();
        // 同步zookeeper
        zkTemplate.deleteNode(ZkTemplate.buildPath(order.getProfileId()));
    }
}
