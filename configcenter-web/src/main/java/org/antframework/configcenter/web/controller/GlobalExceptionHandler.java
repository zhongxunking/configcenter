/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 16:41 创建
 */
package org.antframework.configcenter.web.controller;

import org.antframework.common.util.facade.*;
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

    @ExceptionHandler(BizException.class)
    public AbstractResult handleBizException(BizException e) {
        EmptyResult result = new EmptyResult();
        result.setStatus(e.getStatus());
        result.setCode(e.getCode());
        result.setMessage(e.getMessage());

        return result;
    }

    @ExceptionHandler(Exception.class)
    public AbstractResult handleException(Exception e) {
        logger.error("web层发生异常：", e);

        EmptyResult result = new EmptyResult();
        result.setStatus(Status.PROCESSING);
        result.setCode(CommonResultCode.UNKNOWN_ERROR.getCode());
        result.setMessage(e.getMessage());

        return result;
    }
}
