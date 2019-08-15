/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-12 20:44 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.PropertyValues;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.vo.Property;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 回滚配置value服务
 */
@Service
@AllArgsConstructor
public class RevertPropertyValuesService {
    @ServiceExecute
    public void execute(ServiceContext<RevertPropertyValuesOrder, EmptyResult> context) {
        RevertPropertyValuesOrder order = context.getOrder();
        // 校验入参
        ReleaseInfo release = Releases.findRelease(order.getAppId(), order.getProfileId(), order.getReleaseVersion());
        if (release == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), order.getReleaseVersion()));
        }
        // 删除现有配置value
        PropertyValues.deleteAppProfilePropertyValues(order.getAppId(), order.getProfileId());
        // 使用发布重建配置value
        for (Property property : release.getProperties()) {
            PropertyValues.addOrModifyPropertyValue(
                    order.getAppId(),
                    order.getProfileId(),
                    property.getKey(),
                    property.getValue(),
                    property.getScope());
        }
    }
}
