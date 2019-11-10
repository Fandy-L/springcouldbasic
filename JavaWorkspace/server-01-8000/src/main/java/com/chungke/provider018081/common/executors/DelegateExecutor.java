/*    */
package com.chungke.provider018081.common.executors;
/*    */
///*    */ import com.to8to.common.util.MyThreadFactory;
/*    */

import java.util.concurrent.*;
/*    */
/*    */
/*    */
/*    */ import com.chungke.provider018081.common.util.MyThreadFactory;
import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;


/*    */
/*    */
/*    */ public class DelegateExecutor
        /*    */ implements FastExecutor, CrawlExecutor
        /*    */ {
    /* 16 */   private static final Logger log = LoggerFactory.getLogger(DelegateExecutor.class);
    /*    */
    /*    */   private final ThreadPoolExecutor executor;

    /*    */
    /* 20 */
    public  static  void  mayRun(){

    }

    public DelegateExecutor(int core, int max, int ttl, int queue, String prefix) {
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.execute(DelegateExecutor::mayRun);
//        Executors.newFixedThreadPool();
//        Executors.newScheduledThreadPool();
//        Executors.newSingleThreadScheduledExecutor();
        this.executor = new ThreadPoolExecutor(core, max, ttl, TimeUnit.MINUTES, new ArrayBlockingQueue<>(queue), (ThreadFactory) new MyThreadFactory(prefix));
    }

    /*    */
    /*    */
    /*    */
    /*    */
    /*    */
    public void execute(Runnable command) {
        /* 26 */
        log.debug("async submit task, current pool size:{}, execute task thread count:{}, total task count:{}, queue size:{}, completed task count:{}", new Object[]{
/*    */
/* 28 */           Integer.valueOf(this.executor.getPoolSize()), Integer.valueOf(this.executor.getActiveCount()),
/* 29 */           Long.valueOf(this.executor.getTaskCount()), this.executor.getQueue(),
/* 30 */           Long.valueOf(this.executor.getCompletedTaskCount())});
        /* 31 */
        this.executor.execute(new SafeRunnable(command));
        /*    */
    }

    /*    */
    /*    */
    /*    */
    public void destroy() {
        /* 36 */
        log.info("shutdown delegateExecutor");
        /* 37 */
        this.executor.shutdown();
        /*    */
    }
    /*    */
}


/* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\common\executors\DelegateExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.1
 */