/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-21 23:35 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.DeleteReleaseOrder;
import org.antframework.configcenter.facade.result.DeleteReleaseResult;
import org.antframework.configcenter.facade.vo.ResultCode;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

/**
 * 删除发布服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteReleaseService {
    // info转换器
    private static final Converter<Release, ReleaseInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ReleaseInfo.class);

    // 发布dao
    private final ReleaseDao releaseDao;

    @ServiceExecute
    public void execute(ServiceContext<DeleteReleaseOrder, DeleteReleaseResult> context) {
        DeleteReleaseOrder order = context.getOrder();
        DeleteReleaseResult result = context.getResult();

        Release release = releaseDao.findLockByAppIdAndProfileIdAndVersion(order.getAppId(), order.getProfileId(), order.getVersion());
        if (release != null) {
            if (releaseDao.existsByAppIdAndProfileIdAndParentVersion(order.getAppId(), order.getProfileId(), order.getVersion())) {
                throw new BizException(Status.FAIL, ResultCode.EXISTS_CHILDREN.getCode(), String.format("发布[appId=%s,profileId=%s,version=%d]", order.getAppId(), order.getProfileId(), order.getVersion()));
            }
            releaseDao.delete(release);
            // 设置result
            result.setRelease(INFO_CONVERTER.convert(release));
        }
    }
}
