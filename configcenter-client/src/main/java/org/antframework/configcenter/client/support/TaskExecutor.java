/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-19 18:26 创建
 */
package org.antframework.configcenter.client.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务执行器（如果已存在相同的任务，则会自动忽略）
 */
public class TaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(TaskExecutor.class);

    // 执行任务的线程池
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            0,
            1,
            5,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>() {
                @Override
                public boolean offer(Runnable runnable) {
                    if (contains(runnable)) {
                        return false;
                    }
                    return super.offer(runnable);
                }
            },
            new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Task<?> task) {
        threadPool.execute(task);
    }

    /**
     * 关闭
     */
    public void close() {
        threadPool.shutdown();
    }

    /**
     * 任务
     *
     * @param <T> 目标类型
     */
    public static abstract class Task<T> implements Runnable {
        // 目标对象
        private T target;

        public Task(T target) {
            this.target = target;
        }

        @Override
        public void run() {
            try {
                doRun(target);
            } catch (Throwable e) {
                logger.error("请求配置中心失败：{}", e.getMessage());
            }
        }

        protected abstract void doRun(T target);

        @Override
        public int hashCode() {
            return target.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Task)) {
                return false;
            }
            Task other = (Task) obj;
            return target.equals(other.target);
        }
    }
}
