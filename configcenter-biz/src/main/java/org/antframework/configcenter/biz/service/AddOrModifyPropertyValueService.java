/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 22:42 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyValueOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;

/**
 * 新增或删除配置value服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyPropertyValueService {
    // 应用dao
    private final AppDao appDao;
    // 环境dao
    private final ProfileDao profileDao;
    // 配置value dao
    private final PropertyValueDao propertyValueDao;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyPropertyValueOrder, EmptyResult> context) {
        AddOrModifyPropertyValueOrder order = context.getOrder();
        // 校验入参
        App app = appDao.findLockByAppId(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
        Profile profile = profileDao.findLockByProfileId(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileId()));
        }
        // 新增或修改配置value
        PropertyValue propertyValue = propertyValueDao.findLockByAppIdAndKeyAndProfileId(order.getAppId(), order.getKey(), order.getProfileId());
        if (propertyValue == null) {
            propertyValue = new PropertyValue();
        }
        BeanUtils.copyProperties(order, propertyValue);
        propertyValueDao.save(propertyValue);
    }
}
