/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 16:41 创建
 */
package org.antframework.configcenter.web.controller;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * web全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AntBekitException.class)
    public AbstractResult handleAntBekitException(AntBekitException e) {
        return new AbstractResult() {
            {
                setStatus(e.getStatus());
                setCode(e.getCode());
                setMessage(e.getMessage());
            }
        };
    }

    @ExceptionHandler(Exception.class)
    public AbstractResult handleException(Exception e) {
        logger.error("web层发生异常：", e);
        return new AbstractResult() {
            {
                setStatus(Status.PROCESSING);
                setCode(CommonResultCode.UNKNOWN_ERROR.getCode());
                setMessage(e.getMessage());
            }
        };
    }
}
