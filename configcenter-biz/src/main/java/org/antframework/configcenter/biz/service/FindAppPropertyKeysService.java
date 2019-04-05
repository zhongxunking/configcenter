/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:10 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.FindAppPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindAppPropertyKeysResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找应用的配置key服务
 */
@Service
@AllArgsConstructor
public class FindAppPropertyKeysService {
    // info转换器
    private static final Converter<PropertyKey, PropertyKeyInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(PropertyKeyInfo.class);

    // 配置key dao
    private final PropertyKeyDao propertyKeyDao;

    @ServiceBefore
    public void before(ServiceContext<FindAppPropertyKeysOrder, FindAppPropertyKeysResult> context) {
        FindAppPropertyKeysOrder order = context.getOrder();

        AppInfo app = AppUtils.findApp(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindAppPropertyKeysOrder, FindAppPropertyKeysResult> context) {
        FindAppPropertyKeysOrder order = context.getOrder();
        FindAppPropertyKeysResult result = context.getResult();

        List<PropertyKey> propertyKeys = propertyKeyDao.findByAppId(order.getAppId());
        // 忽略作用域不合要求的key
        propertyKeys.removeIf(propertyKey -> propertyKey.getScope().compareTo(order.getMinScope()) < 0);
        // 设置result
        for (PropertyKey propertyKey : propertyKeys) {
            result.addPropertyKey(INFO_CONVERTER.convert(propertyKey));
        }
    }
}
