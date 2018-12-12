/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-12 20:44 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.*;
import org.antframework.configcenter.biz.util.PropertyValueUtils;
import org.antframework.configcenter.biz.util.ReleaseUtils;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.antframework.configcenter.facade.order.RevertPropertyValuesOrder;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 回滚配置value服务
 */
@Service(enableTx = true)
public class RevertPropertyValuesService {
    @Autowired
    private PropertyValueService propertyValueService;

    @ServiceExecute
    public void execute(ServiceContext<RevertPropertyValuesOrder, EmptyResult> context) {
        RevertPropertyValuesOrder order = context.getOrder();
        // 校验入参
        ReleaseInfo release = null;
        if (order.getReleaseVersion() > ReleaseConstant.ORIGIN_VERSION) {
            release = ReleaseUtils.findRelease(order.getAppId(), order.getProfileId(), order.getReleaseVersion());
            if (release == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), order.getReleaseVersion()));
            }
        }
        // 删除现有配置value
        PropertyValueUtils.deleteAppProfilePropertyValues(order.getAppId(), order.getProfileId());
        // 判断是否需要重建配置value
        if (release == null) {
            return;
        }
        // 使用发布重建配置value
        for (Property property : release.getProperties()) {
            addPropertyValue(order.getAppId(), order.getProfileId(), property);
        }
    }

    // 新增配置value
    private void addPropertyValue(String appId, String profileId, Property property) {
        AddOrModifyPropertyValueOrder order = new AddOrModifyPropertyValueOrder();
        BeanUtils.copyProperties(property, order);
        order.setAppId(appId);
        order.setProfileId(profileId);

        EmptyResult result = propertyValueService.addOrModifyPropertyValue(order);
        FacadeUtils.assertSuccess(result);
    }
}
