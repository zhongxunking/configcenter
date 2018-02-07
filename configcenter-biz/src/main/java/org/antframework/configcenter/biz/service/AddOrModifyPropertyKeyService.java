/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:42 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.common.ZkUtils;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 添加或修改属性key服务
 */
@Service(enableTx = true)
public class AddOrModifyPropertyKeyService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private PropertyKeyDao propertyKeyDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyPropertyKeyOrder, EmptyResult> context) {
        AddOrModifyPropertyKeyOrder order = context.getOrder();

        App app = appDao.findLockByAppCode(order.getAppCode());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppCode()));
        }
        PropertyKey propertyKey = propertyKeyDao.findLockByAppCodeAndKey(order.getAppCode(), order.getKey());
        if (propertyKey == null) {
            propertyKey = buildPropertyKey(order);
        } else {
            propertyKey.setOutward(order.isOutward());
            propertyKey.setMemo(order.getMemo());
        }
        propertyKeyDao.save(propertyKey);
    }

    @ServiceAfter
    public void after(ServiceContext<AddOrModifyPropertyKeyOrder, EmptyResult> context) {
        AddOrModifyPropertyKeyOrder order = context.getOrder();

        List<Profile> profiles = profileDao.findAll();
        for (Profile profile : profiles) {
            zkTemplate.setData(ZkTemplate.buildPath(profile.getProfileCode(), order.getAppCode()), ZkUtils.getCurrentDate());
        }
    }

    // 构建属性key
    private PropertyKey buildPropertyKey(AddOrModifyPropertyKeyOrder addOrModifyPropertyKeyOrder) {
        PropertyKey propertyKey = new PropertyKey();
        BeanUtils.copyProperties(addOrModifyPropertyKeyOrder, propertyKey);
        return propertyKey;
    }
}
