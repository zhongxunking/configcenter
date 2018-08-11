/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:52 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.api.ProfileService;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAllProfilesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 删除属性key服务
 */
@Service(enableTx = true)
public class DeletePropertyKeyService {
    @Autowired
    private PropertyKeyDao propertyKeyDao;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private PropertyValueService propertyValueService;

    @ServiceExecute
    public void execute(ServiceContext<DeletePropertyKeyOrder, EmptyResult> context) {
        DeletePropertyKeyOrder order = context.getOrder();

        PropertyKey propertyKey = propertyKeyDao.findLockByAppIdAndKey(order.getAppId(), order.getKey());
        if (propertyKey == null) {
            return;
        }
        // 删除该key在所有环境的value
        for (ProfileInfo profile : getAllProfiles()) {
            deletePropertyValue(order.getAppId(), order.getKey(), profile.getProfileId());
        }
        // 删除key
        propertyKeyDao.delete(propertyKey);
    }

    // 获取所有环境
    private List<ProfileInfo> getAllProfiles() {
        FindAllProfilesResult result = profileService.findAllProfiles(new EmptyOrder());
        FacadeUtils.assertSuccess(result);
        return result.getProfiles();
    }

    // 删除属性value
    private void deletePropertyValue(String appId, String key, String profileId) {
        SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
        keyValue.setKey(key);
        keyValue.setValue(null);

        SetPropertyValuesOrder order = new SetPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.addKeyValue(keyValue);

        EmptyResult result = propertyValueService.setPropertyValues(order);
        FacadeUtils.assertSuccess(result);
    }
}
