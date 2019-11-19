package com.to8to.tbt.msc.service.impl;

import com.to8to.tbt.msc.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author juntao.guo
 */
@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {
    private ExecutorService executorService;

    @Value("${thread.pool.core.size}")
    private Integer corePoolSize;

    @Value("${thread.pool.max.size}")
    private Integer maximumPoolSize;

    @Value("${thread.pool.keepalive.time}")
    private Long keepAliveTime;

    @Value("${thread.pool.queue.size}")
    private Integer queueSize;

    @PostConstruct
    private void init() {
        executorService = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueSize),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return executorService.submit(runnable);
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }
}
