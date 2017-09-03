/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:42 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.order.manage.AddOrModifyPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.AddOrModifyPropertyKeyResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 添加或修改属性key服务
 */
@Service(enableTx = true)
public class AddOrModifyPropertyKeyService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private PropertyKeyDao propertyKeyDao;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyPropertyKeyOrder, AddOrModifyPropertyKeyResult> context) {
        AddOrModifyPropertyKeyOrder order = context.getOrder();

        App app = appDao.findLockByAppCode(order.getAppCode());
        if (app == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppCode()));
        }
        PropertyKey propertyKey = propertyKeyDao.findLockByAppCodeAndKey(order.getAppCode(), order.getKey());
        if (propertyKey == null) {
            propertyKey = buildPropertyKey(order);
        } else {
            propertyKey.setCommon(order.isCommon());
            propertyKey.setMemo(order.getMemo());
        }
        propertyKeyDao.save(propertyKey);
    }

    // 构建属性key
    private PropertyKey buildPropertyKey(AddOrModifyPropertyKeyOrder addOrModifyPropertyKeyOrder) {
        PropertyKey propertyKey = new PropertyKey();
        BeanUtils.copyProperties(addOrModifyPropertyKeyOrder, propertyKey);
        return propertyKey;
    }
}
