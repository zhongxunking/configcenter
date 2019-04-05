/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 21:44 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.order.FindAppOrder;
import org.antframework.configcenter.facade.result.FindAppResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

/**
 * 查找应用服务
 */
@Service
@AllArgsConstructor
public class FindAppService {
    // info转换器
    private static final Converter<App, AppInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(AppInfo.class);

    // 应用dao
    private final AppDao appDao;

    @ServiceExecute
    public void execute(ServiceContext<FindAppOrder, FindAppResult> context) {
        FindAppOrder order = context.getOrder();
        FindAppResult result = context.getResult();

        App app = appDao.findByAppId(order.getAppId());
        if (app != null) {
            result.setApp(INFO_CONVERTER.convert(app));
        }
    }
}
