/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 13:47 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.facade.info.ManagerInfo;
import org.antframework.configcenter.web.manager.facade.order.ManagerLoginOrder;
import org.antframework.configcenter.web.manager.facade.result.ManagerLoginResult;
import org.apache.commons.lang3.StringUtils;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 管理员登录服务
 */
@Service
public class ManagerLoginService {
    @Autowired
    private ManagerDao managerDao;

    @ServiceExecute
    public void execute(ServiceContext<ManagerLoginOrder, ManagerLoginResult> context) {
        ManagerLoginOrder order = context.getOrder();
        ManagerLoginResult result = context.getResult();

        Manager manager = managerDao.findByCode(order.getCode());
        if (manager == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("管理员[%s]不存在", order.getCode()));
        }
        if (!StringUtils.equals(order.getPassword(), manager.getPassword())) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "密码不正确");
        }
        result.setManagerInfo(buildInfo(manager));
    }

    // 构建info
    private ManagerInfo buildInfo(Manager manager) {
        ManagerInfo info = new ManagerInfo();
        BeanUtils.copyProperties(manager, info);
        return info;
    }
}
