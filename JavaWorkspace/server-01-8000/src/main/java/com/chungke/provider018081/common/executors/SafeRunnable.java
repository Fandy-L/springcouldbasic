package com.chungke.provider018081.common.executors;

//import com.to8to.common.util.BSIDUtils;
import java.util.Objects;

import com.chungke.provider018081.common.util.BSIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public class SafeRunnable
  implements Runnable
{
  private static final Logger log = LoggerFactory.getLogger(SafeRunnable.class);
  private Runnable runnable;
  private String bsid;

  public SafeRunnable(Runnable runnable) {
    Objects.requireNonNull(runnable);
    this.runnable = runnable;
    this.bsid = BSIDUtils.getCurBsid();
    log.info(Thread.currentThread().getName()+"  put BSID TO Runnable  "+MDC.get("bsid"));
  }


  public void run() {
    BSIDUtils.putRelationBsid(this.bsid);
    try {
      Thread.sleep(400);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    log.info(Thread.currentThread().getName()+"  use BSID TO Runnable  "+MDC.get("bsid"));
    try {
      this.runnable.run();
//      throw  new RuntimeException();
    } catch (Exception e) {
      log.error("线程池执行任务异常 error occurred in task", e);
    } finally {
      BSIDUtils.removeRelationBsid();
    }
  }
}


/* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\common\executors\SafeRunnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.1
 */