/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 20:22 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.manage.FindAllProfileOrder;
import org.antframework.configcenter.facade.result.manage.FindAllProfileResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 查找所有环境服务
 */
@Service
public class FindAllProfileService {
    @Autowired
    private ProfileDao profileDao;

    @ServiceExecute
    public void execute(ServiceContext<FindAllProfileOrder, FindAllProfileResult> context) {
        FindAllProfileResult result = context.getResult();

        List<Profile> profiles = profileDao.findAll();
        for (Profile profile : profiles) {
            ProfileInfo info = new ProfileInfo();
            BeanUtils.copyProperties(profile, info);
            result.addInfo(info);
        }
    }
}
