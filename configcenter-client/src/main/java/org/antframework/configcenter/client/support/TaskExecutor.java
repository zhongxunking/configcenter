/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-19 18:26 创建
 */
package org.antframework.configcenter.client.support;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务执行器
 */
@Slf4j
public class TaskExecutor {
    // 执行任务的线程池
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            0,
            1,
            5,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task) {
        threadPool.execute(task);
    }

    /**
     * 关闭
     */
    public void close() {
        threadPool.shutdown();
    }
}
