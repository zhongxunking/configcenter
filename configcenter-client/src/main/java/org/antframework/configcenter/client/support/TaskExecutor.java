/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-08-19 18:26 创建
 */
package org.antframework.configcenter.client.support;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务执行器（如果已存在相同的任务，则会自动忽略）
 */
@Slf4j
public class TaskExecutor {
    // 执行任务的线程池
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
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
    @AllArgsConstructor
    public static abstract class Task<T> implements Runnable {
        // 目标对象
        protected final T target;

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
            return Objects.equals(target, other.target);
        }
    }
}
