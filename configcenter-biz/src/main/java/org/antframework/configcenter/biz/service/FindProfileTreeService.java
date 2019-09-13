/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-10-14 21:18 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Profiles;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ProfileTree;
import org.antframework.configcenter.facade.order.FindProfileTreeOrder;
import org.antframework.configcenter.facade.result.FindProfileTreeResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找环境树服务
 */
@Service
@AllArgsConstructor
public class FindProfileTreeService {
    // info转换器
    private static final Converter<Profile, ProfileInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ProfileInfo.class);

    // 环境dao
    private final ProfileDao profileDao;

    @ServiceExecute
    public void execute(ServiceContext<FindProfileTreeOrder, FindProfileTreeResult> context) {
        FindProfileTreeOrder order = context.getOrder();
        FindProfileTreeResult result = context.getResult();

        ProfileInfo rootProfile = null;
        if (order.getRootProfileId() != null) {
            rootProfile = Profiles.findProfile(order.getRootProfileId());
            if (rootProfile == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getRootProfileId()));
            }
        }
        result.setProfileTree(getProfileTree(rootProfile));
    }

    // 获取环境树
    private ProfileTree getProfileTree(ProfileInfo profile) {
        ProfileTree profileTree = new ProfileTree(profile);

        List<Profile> childrenProfile = profileDao.findByParent(profile == null ? null : profile.getProfileId());
        for (Profile childProfile : childrenProfile) {
            ProfileTree childTree = getProfileTree(INFO_CONVERTER.convert(childProfile));
            profileTree.addChild(childTree);
        }

        return profileTree;
    }
}
