/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 13:47 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.common.PasswordUtils;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.facade.info.ManagerInfo;
import org.antframework.configcenter.web.manager.facade.order.ManagerLoginOrder;
import org.antframework.configcenter.web.manager.facade.result.ManagerLoginResult;
import org.apache.commons.lang3.StringUtils;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * 管理员登录服务
 */
@Service
public class ManagerLoginService {
    // info转换器
    private static final Converter<Manager, ManagerInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ManagerInfo.class);

    @Autowired
    private ManagerDao managerDao;

    @ServiceExecute
    public void execute(ServiceContext<ManagerLoginOrder, ManagerLoginResult> context) {
        ManagerLoginOrder order = context.getOrder();
        ManagerLoginResult result = context.getResult();

        Manager manager = managerDao.findByManagerCode(order.getManagerCode());
        if (manager == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("管理员[%s]不存在", order.getManagerCode()));
        }
        if (!StringUtils.equals(PasswordUtils.digest(order.getPassword()), manager.getPassword())) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "密码不正确");
        }
        result.setManagerInfo(INFO_CONVERTER.convert(manager));
    }
}
