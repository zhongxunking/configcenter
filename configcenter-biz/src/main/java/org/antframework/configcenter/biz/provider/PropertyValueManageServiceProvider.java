/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:03 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.boot.bekit.CommonQueryConstant;
import org.antframework.boot.bekit.CommonQueryResult;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.facade.api.manage.PropertyValueManageService;
import org.antframework.configcenter.facade.order.manage.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.FindAppProfilePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.manage.FindAppProfilePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 属性value管理服务提供者
 */
@Service
public class PropertyValueManageServiceProvider implements PropertyValueManageService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public EmptyResult setPropertyValues(SetPropertyValuesOrder order) {
        return serviceEngine.execute("setPropertyValuesService", order);
    }

    @Override
    public EmptyResult deletePropertyValue(DeletePropertyValueOrder order) {
        return serviceEngine.execute("deletePropertyValueService", order);
    }

    @Override
    public FindAppProfilePropertyValueResult findAppProfilePropertyValue(FindAppProfilePropertyValueOrder order) {
        return serviceEngine.execute("findAppProfilePropertyValueService", order);
    }

    @Override
    public QueryPropertyValueResult queryPropertyValue(QueryPropertyValueOrder order) {
        CommonQueryResult result = serviceEngine.execute(CommonQueryConstant.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachment(PropertyValueDao.class));
        return result.convertTo(QueryPropertyValueResult.class);
    }
}
