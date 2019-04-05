/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:42 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.order.AddOrModifyPropertyKeyOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;

/**
 * 添加或修改配置key服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyPropertyKeyService {
    // 应用dao
    private final AppDao appDao;
    // 配置key dao
    private final PropertyKeyDao propertyKeyDao;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyPropertyKeyOrder, EmptyResult> context) {
        AddOrModifyPropertyKeyOrder order = context.getOrder();

        App app = appDao.findLockByAppId(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
        PropertyKey propertyKey = propertyKeyDao.findLockByAppIdAndKey(order.getAppId(), order.getKey());
        if (propertyKey == null) {
            propertyKey = new PropertyKey();
        }
        BeanUtils.copyProperties(order, propertyKey);

        propertyKeyDao.save(propertyKey);
    }
}
