/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 20:02 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.FindProfileOrder;
import org.antframework.configcenter.facade.result.FindProfileResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

/**
 * 查找环境服务
 */
@Service
@AllArgsConstructor
public class FindProfileService {
    // info转换器
    private static final Converter<Profile, ProfileInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ProfileInfo.class);

    // 环境dao
    private final ProfileDao profileDao;

    @ServiceExecute
    public void execute(ServiceContext<FindProfileOrder, FindProfileResult> context) {
        FindProfileOrder order = context.getOrder();
        FindProfileResult result = context.getResult();

        Profile profile = profileDao.findByProfileId(order.getProfileId());
        if (profile != null) {
            result.setProfile(INFO_CONVERTER.convert(profile));
        }
    }
}
