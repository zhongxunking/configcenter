/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 20:24 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.FindInheritedProfilesOrder;
import org.antframework.configcenter.facade.result.FindInheritedProfilesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * 查找环境继承的所有环境服务
 */
@Service
public class FindInheritedProfilesService {
    // info转换器
    private static final Converter<Profile, ProfileInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ProfileInfo.class);

    @Autowired
    private ProfileDao profileDao;

    @ServiceExecute
    public void execute(ServiceContext<FindInheritedProfilesOrder, FindInheritedProfilesResult> context) {
        FindInheritedProfilesOrder order = context.getOrder();
        FindInheritedProfilesResult result = context.getResult();

        String profileId = order.getProfileId();
        while (profileId != null) {
            Profile profile = profileDao.findByProfileId(profileId);
            if (profile == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("不存在环境[%s]", profileId));
            }
            result.addInheritedProfile(INFO_CONVERTER.convert(profile));

            profileId = profile.getParent();
        }
    }
}
