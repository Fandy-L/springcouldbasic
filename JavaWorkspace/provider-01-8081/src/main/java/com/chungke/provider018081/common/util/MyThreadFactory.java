/*    */ package com.chungke.provider018081.common.util;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class MyThreadFactory implements ThreadFactory {
/*  8 */   private static final Logger log = LoggerFactory.getLogger(MyThreadFactory.class);
/*    */   
/*    */   private final ThreadGroup group;
/*    */   
/* 12 */   private final AtomicInteger threadNumber = new AtomicInteger(1);
/*    */   
/*    */   private final String namePrefix;
/*    */   
/*    */   public MyThreadFactory(String prefix) {
/* 17 */     SecurityManager s = System.getSecurityManager();
/* 18 */     this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
/* 19 */     this.namePrefix = prefix;
/*    */   }
/*    */ 
/*    */   
/*    */   public Thread newThread(Runnable r) {
/* 24 */     String name = this.namePrefix + this.threadNumber.getAndIncrement();
/*    */     
/* 26 */     log.info("newThread: {}", name);
/*    */     
/* 28 */     Thread t = new Thread(this.group, r, name, 0L);
/*    */     
/* 30 */     if (t.isDaemon()) {
/* 31 */       t.setDaemon(false);
/*    */     }
/*    */     
/* 34 */     if (t.getPriority() != 5) {
/* 35 */       t.setPriority(5);
/*    */     }
/*    */     
/* 38 */     return t;
/*    */   }
/*    */ }


/* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\commo\\util\MyThreadFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.1
 */