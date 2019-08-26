/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:26 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.biz.util.Profiles;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValuesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找应用在指定环境的所有配置value服务
 */
@Service
@AllArgsConstructor
public class FindAppProfilePropertyValuesService {
    // info转换器
    private static final Converter<PropertyValue, PropertyValueInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(PropertyValueInfo.class);

    // 配置value dao
    private final PropertyValueDao propertyValueDao;

    @ServiceBefore
    public void before(ServiceContext<FindAppProfilePropertyValuesOrder, FindAppProfilePropertyValuesResult> context) {
        FindAppProfilePropertyValuesOrder order = context.getOrder();

        AppInfo app = Apps.findApp(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
        ProfileInfo profile = Profiles.findProfile(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileId()));
        }
        BranchInfo branch = Branches.findBranch(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindAppProfilePropertyValuesOrder, FindAppProfilePropertyValuesResult> context) {
        FindAppProfilePropertyValuesOrder order = context.getOrder();
        FindAppProfilePropertyValuesResult result = context.getResult();

        List<PropertyValue> propertyValues = propertyValueDao.findByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        // 忽略作用域不合要求的value
        propertyValues.removeIf(propertyValue -> propertyValue.getScope().compareTo(order.getMinScope()) < 0);
        // 设置result
        for (PropertyValue propertyValue : propertyValues) {
            result.addPropertyValue(INFO_CONVERTER.convert(propertyValue));
        }
    }
}
