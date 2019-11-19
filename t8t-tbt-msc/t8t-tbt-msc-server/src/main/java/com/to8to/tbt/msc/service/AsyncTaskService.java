package com.to8to.tbt.msc.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author juntao.guo
 */
public interface AsyncTaskService {
    /**
     * 提交任务
     *
     * @param task
     * @param <T>
     * @return
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * 提交任务
     *
     * @param runnable
     * @return
     */
    Future<?> submit(Runnable runnable);

    /**
     * 关闭线程池
     */
    void shutdown();
}
