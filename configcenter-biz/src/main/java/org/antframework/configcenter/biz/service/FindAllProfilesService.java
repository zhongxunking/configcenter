/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-16 20:22 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.result.FindAllProfilesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找所有环境服务
 */
@Service
public class FindAllProfilesService {
    // info转换器
    private static final Converter<Profile, ProfileInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ProfileInfo.class);

    @Autowired
    private ProfileDao profileDao;

    @ServiceExecute
    public void execute(ServiceContext<EmptyOrder, FindAllProfilesResult> context) {
        FindAllProfilesResult result = context.getResult();

        List<Profile> profiles = profileDao.findAll();
        for (Profile profile : profiles) {
            result.addInfo(INFO_CONVERTER.convert(profile));
        }
    }
}
