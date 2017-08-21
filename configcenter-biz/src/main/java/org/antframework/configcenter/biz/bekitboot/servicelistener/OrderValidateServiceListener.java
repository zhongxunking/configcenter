/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:59 创建
 */
package org.antframework.configcenter.biz.bekitboot.servicelistener;

import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.bekitboot.exception.AntBekitException;
import org.bekit.event.annotation.listener.Listen;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.engine.ServiceContext;
import org.bekit.service.event.ServiceApplyEvent;

/**
 * order校验-服务监听器
 */
@ServiceListener(priority = 4)
public class OrderValidateServiceListener {

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        ServiceContext<AbstractOrder, AbstractResult> serviceContext = event.getServiceContext();
        try {
            serviceContext.getOrder().check();
        } catch (Throwable e) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), CommonResultCode.INVALID_PARAMETER.getMessage() + "：" + e.getMessage());
        }
    }

}
