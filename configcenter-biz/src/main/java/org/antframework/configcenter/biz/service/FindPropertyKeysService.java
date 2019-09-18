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
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.dal.dao.PropertyKeyDao;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyKeyInfo;
import org.antframework.configcenter.facade.order.FindPropertyKeysOrder;
import org.antframework.configcenter.facade.result.FindPropertyKeysResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找配置key集服务
 */
@Service
@AllArgsConstructor
public class FindPropertyKeysService {
    // 转换器
    private static final Converter<PropertyKey, PropertyKeyInfo> CONVERTER = new FacadeUtils.DefaultConverter<>(PropertyKeyInfo.class);

    // 配置key dao
    private final PropertyKeyDao propertyKeyDao;

    @ServiceBefore
    public void before(ServiceContext<FindPropertyKeysOrder, FindPropertyKeysResult> context) {
        FindPropertyKeysOrder order = context.getOrder();
        // 校验入参
        AppInfo app = Apps.findApp(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindPropertyKeysOrder, FindPropertyKeysResult> context) {
        FindPropertyKeysOrder order = context.getOrder();
        FindPropertyKeysResult result = context.getResult();
        // 查找配置key集
        List<PropertyKey> propertyKeys = propertyKeyDao.findByAppId(order.getAppId());
        propertyKeys.stream()
                .filter(propertyKey -> propertyKey.getScope().compareTo(order.getMinScope()) >= 0)
                .map(CONVERTER::convert)
                .forEach(result::addPropertyKey);
    }
}
