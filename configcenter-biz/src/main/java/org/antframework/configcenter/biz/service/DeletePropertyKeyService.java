/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:52 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.zookeeper.ZkTemplate;
import org.antframework.configcenter.common.ZkUtils;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.order.DeletePropertyKeyOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
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
    private PropertyValueDao propertyValueDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private ZkTemplate zkTemplate;

    @ServiceExecute
    public void execute(ServiceContext<DeletePropertyKeyOrder, EmptyResult> context) {
        DeletePropertyKeyOrder order = context.getOrder();

        PropertyKey propertyKey = propertyKeyDao.findLockByAppCodeAndKey(order.getAppCode(), order.getKey());
        if (propertyKey == null) {
            return;
        }
        if (propertyValueDao.existsByAppCodeAndKey(order.getAppCode(), order.getKey())) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("应用[%s]的属性key[%s]还存在属性值，不能删除", order.getAppCode(), order.getKey()));
        }

        propertyKeyDao.delete(propertyKey);
    }

    @ServiceAfter
    public void after(ServiceContext<DeletePropertyKeyOrder, EmptyResult> context) {
        DeletePropertyKeyOrder order = context.getOrder();

        List<Profile> profiles = profileDao.findAll();
        for (Profile profile : profiles) {
            zkTemplate.setData(ZkTemplate.buildPath(profile.getProfileCode(), order.getAppCode()), ZkUtils.getCurrentDate());
        }
    }
}
