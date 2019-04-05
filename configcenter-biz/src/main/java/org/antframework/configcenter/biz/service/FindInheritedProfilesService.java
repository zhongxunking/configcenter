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
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.FindInheritedProfilesOrder;
import org.antframework.configcenter.facade.result.FindInheritedProfilesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找环境继承的所有环境服务
 */
@Service
public class FindInheritedProfilesService {
    @ServiceExecute
    public void execute(ServiceContext<FindInheritedProfilesOrder, FindInheritedProfilesResult> context) {
        FindInheritedProfilesOrder order = context.getOrder();
        FindInheritedProfilesResult result = context.getResult();

        String profileId = order.getProfileId();
        while (profileId != null) {
            ProfileInfo profile = ProfileUtils.findProfile(profileId);
            if (profile == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", profileId));
            }
            result.addInheritedProfile(profile);

            profileId = profile.getParent();
        }
    }
}
