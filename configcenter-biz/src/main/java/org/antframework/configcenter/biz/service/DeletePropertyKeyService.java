/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:52 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.enums.ResultCode;
import org.antframework.configcenter.facade.order.manage.DeletePropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.DeletePropertyKeyResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service(enableTx = true)
public class DeletePropertyKeyService {
    @Autowired
    private PropertyKeyDao propertyKeyDao;
    @Autowired
    private PropertyValueDao propertyValueDao;

    @ServiceExecute
    public void execute(ServiceContext<DeletePropertyKeyOrder, DeletePropertyKeyResult> serviceContext) {
        DeletePropertyKeyOrder order = serviceContext.getOrder();

        PropertyKey propertyKey = propertyKeyDao.findLockByAppCodeAndKey(order.getAppCode(), order.getKey());
        if (propertyKey == null) {
            return;
        }
        if (propertyValueDao.existsByAppCodeAndKey(order.getAppCode(), order.getKey())) {
            throw new AntBekitException(Status.FAIL, ResultCode.ILLEGAL_STATE.getCode(), String.format("应用[%s]的属性key[%s]还存在属性值，不能删除", order.getAppCode(), order.getKey()));
        }

        propertyKeyDao.delete(propertyKey);
    }

}
