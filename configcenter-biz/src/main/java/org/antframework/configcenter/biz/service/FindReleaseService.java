/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-12 21:05 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.ProfileUtils;
import org.antframework.configcenter.biz.util.ReleaseUtils;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.FindReleaseOrder;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

/**
 * 查找发布服务
 */
@Service
@AllArgsConstructor
public class FindReleaseService {
    // info转换器
    private static final Converter<Release, ReleaseInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ReleaseInfo.class);

    // 发布dao
    private final ReleaseDao releaseDao;

    @ServiceBefore
    public void before(ServiceContext<FindReleaseOrder, FindReleaseResult> context) {
        FindReleaseOrder order = context.getOrder();
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
    public void execute(ServiceContext<FindReleaseOrder, FindReleaseResult> context) {
        FindReleaseOrder order = context.getOrder();
        FindReleaseResult result = context.getResult();

        Release release = releaseDao.findByAppIdAndProfileIdAndVersion(order.getAppId(), order.getProfileId(), order.getVersion());
        if (release != null) {
            result.setRelease(INFO_CONVERTER.convert(release));
        } else if (order.getVersion() == ReleaseConstant.ORIGIN_VERSION) {
            result.setRelease(ReleaseUtils.buildOriginRelease(order.getAppId(), order.getProfileId()));
        }
    }
}
