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
import org.antframework.configcenter.biz.util.Branches;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.FindPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindPropertyValuesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找配置value集服务
 */
@Service
@AllArgsConstructor
public class FindPropertyValuesService {
    // 转换器
    private static final Converter<PropertyValue, PropertyValueInfo> CONVERTER = new FacadeUtils.DefaultConverter<>(PropertyValueInfo.class);

    // 配置value dao
    private final PropertyValueDao propertyValueDao;

    @ServiceBefore
    public void before(ServiceContext<FindPropertyValuesOrder, FindPropertyValuesResult> context) {
        FindPropertyValuesOrder order = context.getOrder();
        // 校验入参
        BranchInfo branch = Branches.findBranch(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindPropertyValuesOrder, FindPropertyValuesResult> context) {
        FindPropertyValuesOrder order = context.getOrder();
        FindPropertyValuesResult result = context.getResult();
        // 查找配置value集
        List<PropertyValue> propertyValues = propertyValueDao.findByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        propertyValues.stream()
                .filter(propertyValue -> propertyValue.getScope().compareTo(order.getMinScope()) >= 0)
                .map(CONVERTER::convert)
                .forEach(result::addPropertyValue);
    }
}
