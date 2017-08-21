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
import org.bekit.service.event.ServiceExceptionEvent;

/**
 * result维护-服务监听器
 */
@ServiceListener(priority = 3)
public class ResultMaintainServiceListener {

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        ServiceContext<AbstractOrder, AbstractResult> serviceContext = event.getServiceContext();
        initResult(serviceContext.getResult());
    }

    @Listen(priorityAsc = false)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        ServiceContext<AbstractOrder, AbstractResult> serviceContext = event.getServiceContext();

        Throwable throwable = event.getTargetException();
        if (throwable instanceof AntBekitException) {
            setResultByAntBekitException(serviceContext.getResult(), (AntBekitException) throwable);
        } else {
            setResultByUnknownException(serviceContext.getResult(), throwable);
        }
    }

    // 初始化result
    private void initResult(AbstractResult result) {
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
    }

    // 依据AntBekitException设置result
    private void setResultByAntBekitException(AbstractResult result, AntBekitException antBekitException) {
        result.setStatus(antBekitException.getStatus());
        result.setCode(antBekitException.getCode());
        result.setMessage(antBekitException.getMessage());
    }

    // 依据未知异常设置result
    private void setResultByUnknownException(AbstractResult result, Throwable throwable) {
        result.setStatus(Status.PROCESSING);
        result.setCode(CommonResultCode.UNKNOWN_ERROR.getCode());
        result.setMessage(throwable.getMessage());
    }
}
