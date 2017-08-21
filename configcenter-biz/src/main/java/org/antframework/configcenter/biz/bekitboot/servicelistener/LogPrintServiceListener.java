/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:59 创建
 */
package org.antframework.configcenter.biz.bekitboot.servicelistener;

import org.antframework.configcenter.biz.bekitboot.exception.AntBekitException;
import org.bekit.event.annotation.listener.Listen;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceExceptionEvent;
import org.bekit.service.event.ServiceFinishEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志打印-服务监听器
 */
@ServiceListener(priority = 2)
public class LogPrintServiceListener {
    private static final Logger logger = LoggerFactory.getLogger(LogPrintServiceListener.class);

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        logger.info("收到请求：serviceName={}, order={}", event.getService(), event.getServiceContext().getOrder());
    }

    @Listen(priorityAsc = false)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        Throwable throwable = event.getTargetException();
        if (throwable instanceof AntBekitException) {
            AntBekitException antBekitException = (AntBekitException) throwable;
            logger.warn("收到手动异常：status={}, code={}, message={}", antBekitException.getStatus(), antBekitException.getCode(), antBekitException.getMessage());
        } else {
            logger.error("服务执行中发生未知异常：", throwable);
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        logger.info("执行结果：serviceName={}, result={}", event.getService(), event.getServiceContext().getResult());
    }
}
