/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:10 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.manage.FindAppPropertyKeyOrder;
import org.antframework.configcenter.facade.result.manage.FindAppPropertyKeyResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceCheck;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找应用所有的属性key服务
 */
@Service
public class FindAppPropertyKeyService {
    // info转换器
    private static final Converter<PropertyKey, PropertyKeyInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(PropertyKeyInfo.class);

    @Autowired
    private AppDao appDao;
    @Autowired
    private PropertyKeyDao propertyKeyDao;

    @ServiceCheck
    public void check(ServiceContext<FindAppPropertyKeyOrder, FindAppPropertyKeyResult> context) {
        FindAppPropertyKeyOrder order = context.getOrder();

        App app = appDao.findByAppCode(order.getAppCode());
        if (app == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppCode()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindAppPropertyKeyOrder, FindAppPropertyKeyResult> context) {
        FindAppPropertyKeyOrder order = context.getOrder();
        FindAppPropertyKeyResult result = context.getResult();

        List<PropertyKey> propertyKeys = propertyKeyDao.findByAppCode(order.getAppCode());
        for (PropertyKey propertyKey : propertyKeys) {
            result.addInfo(INFO_CONVERTER.convert(propertyKey));
        }
    }
}
