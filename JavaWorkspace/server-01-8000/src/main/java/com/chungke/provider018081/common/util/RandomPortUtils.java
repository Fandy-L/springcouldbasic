/*    */ package com.chungke.provider018081.common.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ThreadLocalRandom;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RandomPortUtils
/*    */ {
/* 15 */   private static final Logger log = LoggerFactory.getLogger(RandomPortUtils.class);
/*    */   
/*    */   private static final String JAVA_SERVER_BIND_PORT_RANGE = "JAVA_SERVER_BIND_PORT_RANGE";
/*    */   private static final int DEFAULT_MIN_PORT = 40001;
/*    */   private static final int DEFAULT_MAX_PORT = 40999;
/*    */   private static final int DEFAULT_PORT_MAX_RETRABLE = 100;
/* 21 */   private static final Set<Integer> USED = new HashSet<>();
/*    */   
/*    */   public static synchronized String getAvailablePort() {
/* 24 */     int minPort = 40001;
/* 25 */     int maxPort = 40999;
/*    */     try {
/* 27 */       String portRange = System.getenv("JAVA_SERVER_BIND_PORT_RANGE");
/* 28 */       if (portRange != null) {
/* 29 */         minPort = Integer.valueOf(portRange.split("-")[0].trim()).intValue();
/* 30 */         maxPort = Integer.valueOf(portRange.split("-")[1].trim()).intValue();
/*    */       } 
/* 32 */     } catch (Exception e) {
/* 33 */       log.warn("解析{}范围端口失败，", "JAVA_SERVER_BIND_PORT_RANGE", e);
/*    */     } 
/*    */     
/* 36 */     for (int i = 0; i < 100; i++) {
/* 37 */       int backup = ThreadLocalRandom.current().nextInt(minPort, maxPort);
/* 38 */       if (!USED.contains(Integer.valueOf(backup)))
/*    */       {
/*    */         
/* 41 */         if (available(backup)) {
/* 42 */           USED.add(Integer.valueOf(backup));
/* 43 */           return String.valueOf(backup);
/*    */         }  } 
/*    */     } 
/* 46 */     String err = "无法获取指定的端口";
/* 47 */     log.info("{}", err);
/* 48 */     System.out.println(err);
/* 49 */     throw new RuntimeException(err);
/*    */   }
/*    */   
/*    */   private static boolean available(int port) {
/* 53 */     try (Socket ignored = new Socket("localhost", port)) {
/* 54 */       return false;
/* 55 */     } catch (IOException ignored) {
/* 56 */       return true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\commo\\util\RandomPortUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.1
 */