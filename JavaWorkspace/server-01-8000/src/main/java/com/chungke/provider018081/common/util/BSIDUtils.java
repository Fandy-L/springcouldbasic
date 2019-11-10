/*    */ package com.chungke.provider018081.common.util;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.slf4j.MDC;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BSIDUtils
/*    */ {
/* 12 */   private static final Logger log = LoggerFactory.getLogger(BSIDUtils.class);
/*    */
/*    */   private static final String BSID = "bsid";
/*    */   
/*    */   public static String getCurBsid() {
/* 17 */
        String old = MDC.get("bsid");
/* 18 */     String news = generatorBsid();
/* 19 */     if (old == null) {
/* 20 */       log.debug("未获取到父线程的bsid，从新生成一个新的bsid：[{}]", news);
/*    */     } else {
/* 22 */       log.debug("获取到父线程的bsid：[{}]", old);
/*    */     } 
/* 24 */     return (old == null) ? news : old;
/*    */   }
/*    */ 
/*    */   
/* 28 */   private static String generatorBsid() { return UUID.randomUUID().toString(); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static void putRelationBsid(String bsid) { MDC.put("bsid", bsid); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public static void removeRelationBsid() { MDC.remove("bsid"); }
/*    */ }


/* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\commo\\util\BSIDUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.1
 */