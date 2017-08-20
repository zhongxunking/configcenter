/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:40 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.order.manage.DeleteProfileOrder;
import org.antframework.configcenter.facade.result.manage.DeleteProfileResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service(enableTx = true)
public class DeleteProfileService {
    @Autowired
    private ProfileDao profileDao;

    @ServiceExecute
    public void serviceExecute(ServiceContext<DeleteProfileOrder, DeleteProfileResult> serviceContext) {
        DeleteProfileOrder order = serviceContext.getOrder();

        Profile profile = profileDao.findLockByProfileCode(order.getProfileCode());
        if (profile != null) {
            profileDao.delete(profile);
        }
    }

}
