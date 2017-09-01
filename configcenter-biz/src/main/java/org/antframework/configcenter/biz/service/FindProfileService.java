/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:44 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.manage.FindProfileOrder;
import org.antframework.configcenter.facade.result.manage.FindProfileResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service
public class FindProfileService {
    @Autowired
    private ProfileDao profileDao;

    @ServiceExecute
    public void execute(ServiceContext<FindProfileOrder, FindProfileResult> serviceContext) {
        FindProfileOrder order = serviceContext.getOrder();
        FindProfileResult result = serviceContext.getResult();

        Profile profile = profileDao.findByProfileCode(order.getProfileCode());
        if (profile != null) {
            result.setProfileInfo(buildProfileInfo(profile));
        }
    }

    private ProfileInfo buildProfileInfo(Profile profile) {
        ProfileInfo info = new ProfileInfo();
        BeanUtils.copyProperties(profile, info);
        return info;
    }
}
