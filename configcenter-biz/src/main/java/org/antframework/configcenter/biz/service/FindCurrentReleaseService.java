/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-09 17:37 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindCurrentReleaseOrder;
import org.antframework.configcenter.facade.result.FindCurrentReleaseResult;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;

/**
 * 查找当前发布服务
 */
@Service
public class FindCurrentReleaseService {
    // info转换器
    private static final Converter<Release, ReleaseInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ReleaseInfo.class);

    @Autowired
    private ReleaseDao releaseDao;

    @ServiceBefore
    public void before(ServiceContext<FindCurrentReleaseOrder, FindCurrentReleaseResult> context) {
        FindCurrentReleaseOrder order = context.getOrder();
        // 校验入参
        AppInfo app = AppUtils.findApp(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
        ProfileInfo profile = ProfileUtils.findProfile(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindCurrentReleaseOrder, FindCurrentReleaseResult> context) {
        FindCurrentReleaseOrder order = context.getOrder();
        FindCurrentReleaseResult result = context.getResult();

        Release release = releaseDao.findFirstByAppIdAndProfileIdOrderByVersionDesc(order.getAppId(), order.getProfileId());
        if (release != null) {
            result.setRelease(INFO_CONVERTER.convert(release));
        } else {
            result.setRelease(buildOriginRelease(order.getAppId(), order.getProfileId()));
        }
    }

    // 构建原始发布
    private static ReleaseInfo buildOriginRelease(String appId, String profileId) {
        ReleaseInfo release = new ReleaseInfo();
        release.setAppId(appId);
        release.setProfileId(profileId);
        release.setVersion(ReleaseConstant.ORIGIN_VERSION);
        release.setMemo(null);
        release.setProperties(new ArrayList<>());

        return release;
    }
}
